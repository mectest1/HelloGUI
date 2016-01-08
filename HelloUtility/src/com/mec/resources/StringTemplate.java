package com.mec.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.xml.internal.ws.policy.sourcemodel.attach.ExternalAttachmentsUnmarshaller;

/**
 * A string template class. It is typically used to parse single line of string.
 * @author MEC
 *
 */
public class StringTemplate {

	
	private StringTemplate(){
		
	}
	
	/**
	 * Get a Regular expression extractor, which will extract different parts 
	 * from a string and reorganize & combine this parts and output a string.
	 * @return a RegExtractor
	 */
	public static RegExtractor getRegExtractor(){
		return regExtractor;
	}
	
	private static final RegExtractor regExtractor = new RegExtractorImpl();
	
	public static interface RegExtractor{
		/**
		 * Match the specified <code>inputStr</code> with pattern <code>extractPattern</code>, then re-organize the matched parts 
		 * according to <code>outputFormat</code> and <code>outputIndices</code> in sequence and 
		 * return the concatenated parts as a whole string.
		 * <dl>
		 * <dt>INPUT:</dt>
		 * 	<dd>ServerJS.java - CSEECore/src/com/cs/core/parser (2 matches)</dd>
		 * 	<dd>"^\\s*([/]?\\w+(/\\w+)+)/(\\w+\\.\\w+)\\s*$"</dd>
		 *	<dd>%s/%s</dd>
		 * 	<dd>2, 1</dd>
		 * <dt>OUTPUT:</dt>
		 * 	<dd>CSEECore/src/com/cs/core/parser/ServerJS.java</dd>
		 * </dl>
		 * @param extractPattern the matching pattern what matches the <strong>whole</strong> <code>inputStr</code>
		 * @param inputStr the input string
		 * @param outputFormat the output format string
		 * @param outputIndices the output indices
		 * @return empty value if the <code>inputStr</code> dosn't match <code>extractPattern</code>
		 */
		Optional<String> extractAndOutput(String inputStr, Pattern extractPattern, String outputFormat, List<Integer> outputIndices) 
				throws IllegalArgumentException;
		
		/**
		 * Try to apply {@link #extractAndOutput(String, Pattern, String, List)} 
		 * for each extract pattern listed in <code>patterns</code>
		 * on <code>inputStr</code>. The error message will be thrown when there is no match found.
		 * @param extractPatterns
		 * @param inputStr
		 * @param outputFormat
		 * @param outputIndices
		 * @param errorMsg
		 * @return
		 * @throws IllegalArgumentException
		 */
		default String extractAndOutput(String inputStr, List<Pattern> extractPatterns, List<String> outputFormats, List<List<Integer>> outputIndicesList, String errorMsg) 
				throws IllegalArgumentException{
			Optional<String> retval = null;
//			for(Pattern extractPattern : extractPatterns){
			for(int i = 0 ; i < extractPatterns.size(); ++i){
				Pattern extractPattern = extractPatterns.get(i);
				String outputFormat = outputFormats.get(i);
				List<Integer> outputIndices = outputIndicesList.get(i);
				retval = extractAndOutput(inputStr, extractPattern, outputFormat, outputIndices);
				if(retval.isPresent()){
					break;
				}
			}
			
			if(!retval.isPresent()){
				throw new IllegalArgumentException(errorMsg);
			}
			return retval.get();
		}
	}
	
	private static class RegExtractorImpl implements RegExtractor{

		@Override
		public Optional<String> extractAndOutput(String inputStr, Pattern extractPattern, String outputFormat,
				List<Integer> outputIndices) throws IllegalArgumentException {
			Optional<String> retval = Optional.empty();
			if(null == extractPattern || null == inputStr){
				return retval;
			}
			
			Matcher m = extractPattern.matcher(inputStr);
			
			if(!m.find()){
				return retval;
			};
			
			List<String> outputParts;
			if(null == outputIndices){
				outputParts = Arrays.asList(m.group(0));
			}else{
//				outputParts = new ArrayList<>();
//				for(int outputIndex : outputIndices){
//					outputParts.add(m.group(outputIndex));
//				}
				outputParts = outputIndices.stream().map(index -> m.group(index))
						.collect(Collectors.toList());
			}
			retval = Optional.of(String.format(outputFormat, outputParts.toArray()));
			
			return retval;
		}
		
	}
}
