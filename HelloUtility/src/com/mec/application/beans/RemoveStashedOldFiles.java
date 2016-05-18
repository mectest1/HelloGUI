package com.mec.application.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;

/**
 * A sample customized PatchProcessAction, used to remove stashed old patch files.
 * @author MEC
 *
 */
public class RemoveStashedOldFiles implements PatchProcessAction{
	private MsgLogger logger;
	public RemoveStashedOldFiles(MsgLogger logger){
		this.logger = logger;
	}
	
	@Override
	public void proceed(PatchProcessContext ppctx) throws Exception {
		Path releaseDirectory = ppctx.getTargetDirectory();
		Path stashDirectory = ppctx.getStashDirectory();
		deleteDirectory(releaseDirectory.resolve(stashDirectory));
	}
	
	private void deleteDirectory(Path path){
		try {
			//		try(Stream<Path> ps = Files.walk(path)){
			//			ps.forEach(p -> {
			//				try {
			//					if (Files.isDirectory(p)) {
			//						deleteDirectory(p);
			//						Files.delete(p);
			//					} else {
			//						Files.delete(p);
			//					} 
			//				} catch (IOException e) {
			//					logger.log(e);
			//				}
			//			});
			//		}
			if (!Files.isDirectory(path)) {
				Files.deleteIfExists(path);
				logger.log(Msg.get(this, "info.deleteFile"), path.toString());
			} else {
//				Stream<Path> subPaths = Files.walk(path);
				Stream<Path> subPaths = Files.list(path);
				subPaths.forEach(this::deleteDirectory);
				subPaths.close();
				Files.delete(path);
				logger.log(Msg.get(this, "info.deleteDirectory"), path.toString());
			} 
		} catch (IOException e) {
			logger.log(e);
		}
	}
	

	@Override
	public String getName() {
		return this.getClass().getName();
	}
	
}
