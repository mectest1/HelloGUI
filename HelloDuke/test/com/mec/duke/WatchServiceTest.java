package com.mec.duke;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import org.junit.Ignore;
import org.junit.Test;

public class WatchServiceTest {

	@Ignore
	@Test
	public void testWatchDirectory() throws Exception{
		Path tmpDir= FileVisitorTest.createTempDir(Paths.get("."));
		
		DirectoryWatch dirWatch = DirectoryWatch.newInstance();
//		dirWatch.watchDirectory(tmpDir, false);
		dirWatch.watchDirectory(tmpDir, true);
		
		ForkJoinPool.commonPool().submit(() -> {
			try {
				Path p = Files.createTempDirectory(tmpDir, "tmp2_"); //<- Q: Why this operation won't trigger Watch Event?
				p = Files.createTempFile(tmpDir, "tmp2", "derp_");
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		});
		
//		Files.deleteIfExists(p);
//		Path p = Files.createTempDirectory(tmpDir, "tmp2_"); //<- Q: Why this operation won't trigger Watch Event?
//		p = Files.createTempFile(tmpDir, "tmp2", "derp_");
		
	
		Thread.sleep(30*1000);
//		out.println("Wait for 5 seconds.");
		FileVisitorTest.deleteFile(tmpDir);
		Thread.sleep(5*1000);
	}

//	@Ignore
	@Test
	public void testRecursiveWatch() throws Exception{
		Path tmpDir= FileVisitorTest.createTempDir(Paths.get("."));
		
		DirectoryRecursiveWatch watch = DirectoryRecursiveWatch.newInstance();
		ForkJoinPool.commonPool().submit(() -> watch.watchDirectory(tmpDir));	//<- Watch in the background;
		
		Thread.sleep(2 * 1000);
//		watch.cancelWatch();	//<-- Without close the WatchService, the watched path will not be able to be deleted;
		FileVisitorTest.deleteFile(tmpDir);
	}
	
	public static class DirectoryRecursiveWatch{
//		private static final ForkJoinPool fj = ForkJoinPool.commonPool();
		private Set<WatchKey> keys = new HashSet<>();
		WatchService watchService;
		
		private List<WatchEventListener<Path>> listeners = new ArrayList<>();
		
		
		public static DirectoryRecursiveWatch newInstance(){
			return new DirectoryRecursiveWatch();
		}

		private void cancelWatch(){
			keys.forEach(key -> key.cancel());
			try {
				watchService.close();
				out.printf("WatchService %s closed.", watchService);		//<-----WatchService should be closed;
				watchService = null;
			} catch (Exception e) {
				e.printStackTrace(out);
			}
			
		}
		public void watchDirectory(Path path){
			try {
				//register the received path
				if (null == watchService) {
					watchService = FileSystems.getDefault().newWatchService();
				}
				registerTree(path);
				processWatchKey();
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		}
		
		public void watchDirectoryAsync(Path path, ExecutorService es){
			try {
				//register the received path
				if (null == watchService) {
					watchService = FileSystems.getDefault().newWatchService();
				}
				registerTree(path);
				es.execute(() -> {
					try {
						processWatchKey();
					} catch (Exception e) {
						e.printStackTrace(out);
					}
				});
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		}
		
		private void registerTree(Path start) throws IOException{
			Files.walkFileTree(start, new SimpleFileVisitor<Path>(){

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					out.printf("Registering watch service for %s\n", dir);
					
					registerPath(dir);
					
					return FileVisitResult.CONTINUE;
				}
				
			});
		}
		
		private void registerPath(Path path) throws IOException{
			keys.add(path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE
					, StandardWatchEventKinds.ENTRY_MODIFY
					, StandardWatchEventKinds.ENTRY_DELETE
					));
		}
		
		
		@SuppressWarnings("unchecked")
		private void processWatchKey() throws IOException, InterruptedException{
//			try(WatchService watchService = path.getFileSystem().newWatchService();){
//			try{
//				keys.add(path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE
//						, StandardWatchEventKinds.ENTRY_MODIFY
//						, StandardWatchEventKinds.ENTRY_DELETE
//						));
			while(true){
				WatchKey key = watchService.take();
				for(WatchEvent<?> evt :  key.pollEvents()){
					WatchEvent<Path> event = (WatchEvent<Path>) evt;
//						Kind<Path> kind = event.kind();
					
					if(StandardWatchEventKinds.OVERFLOW == evt.kind()){
						continue;
					}
					Path dir = (Path) key.watchable();
					Path fileName = event.context();
					Path fullFileName = dir.resolve(fileName);
					if(!Files.isReadable(dir.resolve(fileName))){
						continue;	//In case current file is inaccessible for now.
					}
					if(StandardWatchEventKinds.ENTRY_CREATE == evt.kind()){
//						Path dir = (Path) key.watchable();
//						Path fileName = event.context();
//						if(!Files.isReadable(dir.resolve(fileName))){
//							continue;	//In case current file is inaccessible for now.
//						}
						if(Files.isDirectory(fullFileName)){
//							keys.add(dir.resolve(fileName).register(watchService, StandardWatchEventKinds.ENTRY_CREATE
//									, StandardWatchEventKinds.ENTRY_MODIFY
//									, StandardWatchEventKinds.ENTRY_DELETE
//									));
							registerTree(dir);
						}
//						else{
//							registerPath(dir);
//						}
						
						listeners.stream().forEachOrdered(l -> l.onEntryCreated(fullFileName));
					}else if(StandardWatchEventKinds.ENTRY_DELETE == evt.kind()){
//						Path dir = (Path) key.watchable();
//						Path fileName = event.context();
//						if(!Files.isReadable(dir.resolve(fileName))){
//							continue;	//In case current file is inaccessible for now.
//						}
						if(Files.isDirectory(fullFileName)){
							key.cancel();
						}
						
						listeners.stream().forEachOrdered(l -> l.onEntryDeleted(fullFileName));
					}else if(StandardWatchEventKinds.ENTRY_MODIFY == evt.kind()){
						listeners.stream().forEachOrdered(l -> l.onEntryModified(fullFileName));
					}
					out.printf("%s -> %s\n", event.kind(), event.context());
				}
				
				if(!key.reset()){
					out.printf("WatchKey for %s is invalid now.\n", key.watchable());
					keys.remove(key);
				}
				if(keys.isEmpty()){
					break;
				}
			}
			cancelWatch(); //<- cancel watch after there is no WatchKey anymore.
//			}
		}
		
		
		
		public static  interface WatchEventListener<T>{
			void onEntryCreated(T fullPath);
			void onEntryModified(T fullPath);
			void onEntryDeleted(T fullPath);
		}
	}
	
	static class DirectoryWatch{
//		private static final ExecutorService es = Executors.newCachedThreadPool();
		private static final ForkJoinPool fj = ForkJoinPool.commonPool();
		private Set<WatchKey> watchKeys = new HashSet<>();
		static DirectoryWatch newInstance(){
			return new DirectoryWatch();
		}
		
		public void watchDirectory(Path path, boolean async) throws IOException, InterruptedException{
			if(async){
				fj.submit(() -> processWatchKey(path));
			}else{
				processWatchKey(path);
			}
		}
		
		public void cancelAllWatches(){
			watchKeys.forEach(key -> key.cancel());
		}
		
//		public void cancel(WatchKey key){
//			
//		}
		
		@SuppressWarnings("unchecked")
		void processWatchKey(Path path){
			try(WatchService watcher= FileSystems.getDefault().newWatchService();){
				path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE
						, StandardWatchEventKinds.ENTRY_MODIFY
						, StandardWatchEventKinds.ENTRY_DELETE
						);
				while(true){
					//retrieve and remove the next key
					final WatchKey key = watcher.take();
					watchKeys.add(key);
					
					//get list of pending events for the watch key
					for(WatchEvent<?> event :key.pollEvents()){
//						WatchEvent<Path> event = (WatchEvent<Path>) e; 
						
						//get the kind of event (create, modify, delete);
						final Kind<?> kind = event.kind();
						
						//Handle OVERFLOW event
						if(StandardWatchEventKinds.OVERFLOW == kind){
							continue;
						}
						
						//Get the file name for the event
						final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) event;
						final Path fileName = watchEventPath.context();
						
						out.printf("%s -> %s\n", kind, fileName);
					}
				
					boolean valid = key.reset();
					//exit loop if the key is not valid (if the directory was deleted, for example
					if(!valid){
						out.printf("Watchkey for %s is invalid.\n", key.watchable());
						break;
					}
				}
				
				
			}catch(Exception e){
				e.printStackTrace(out);
			}
		}
	}
	
	
	
	private static final PrintStream out = System.out;
}
