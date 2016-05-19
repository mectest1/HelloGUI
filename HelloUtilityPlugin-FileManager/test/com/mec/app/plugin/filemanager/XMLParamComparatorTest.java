package com.mec.app.plugin.filemanager;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.mec.app.plugin.filemanager.resources.MsgLogger;
import com.mec.app.plugin.filemanager.resources.XMLStructComparator;
import com.mec.app.plugin.filemanager.resources.XMLTools;

public class XMLParamComparatorTest {

	
	
	@Ignore
	@Test
	public void testCompareXMLStructureDirectory() throws Exception{
		fail("Not yet implemented");
		
//		compareFilesInFolder();
//		compareXMLStructure();
	}

	@Ignore
	@Test
	public void testCompareFilesInFolder() throws Exception{
		Path dir1 = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module/1/HSBC/US/WEB/VCH");
		Path dir2 = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module/2/HSBC/US/WEB/VCH");
		
		compareFilesInFolder(dir1, dir2);
	}
	
	
	/**
	 * Check whether two directories contains same files
	 * @param dir1
	 * @param dir2
	 */
	void compareFilesInFolder(Path dir1, Path dir2) throws IOException{
//		try(Stream<Path> sub1 = Files.list(dir1);
//				Stream<Path> sub2 = Files.list(dir2)){
//			
//		}
		List<Path> subPaths1 = Files.list(dir1).collect(Collectors.toList());
		List<Path> subPaths2 = Files.list(dir2).collect(Collectors.toList());
		
//		List<String> subPaths1FileNames = subPaths1.stream().map(p -> p.getFileName().toString()).collect(Collectors.toList());
//		List<String> subPaths2FileNames = subPaths2.stream().map(p -> p.getFileName().toString()).collect(Collectors.toList());
		List<Path> subPaths1FileNames = subPaths1.stream().map(Path::getFileName).collect(Collectors.toList());
		List<Path> subPaths2FileNames = subPaths2.stream().map(Path::getFileName).collect(Collectors.toList());
		
		
		
		subPaths1FileNames.stream().forEach(s1 -> {
			if(!subPaths2FileNames.contains(s1)){
//				out.printf("%s doesn't contains %s that resides in %s\n", dir2.toString(), s1, dir1.toString());
				logger.ln("%s doesn't contains %s that resides in %s\n", dir2.toString(), s1, dir1.toString());
			}
		});
		subPaths2FileNames.stream().forEach(s2 -> {
			if(!subPaths1FileNames.contains(s2)){
				logger.ln("%s doesn't contains %s that resides in %s\n", dir1.toFile(), s2, dir2.toString());
			}
		});
		
//		if(!subPaths1.containsAll(subPaths2)){
//		subPaths2.stream().filter(subPath2 -> {
//			
//		})
//		}
		logger.ln("");
	}
	
	@Ignore
	@Test
	public void testXMLTools(){
		Node n = null;
		logger.ln("Convert null node");
		logger.ln(XMLTools.inst().nodeToStr(n));	//result: <?xml version="1.0" encoding="UTF-8" standalone="no"?>

	}
	
	@Ignore
	@Test
	public void testCompareXMLStructure() throws Exception{
		Path xml1 = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module/1/HSBC/US/WEB/VCH/vch_TEST_TestMoveMoney.xml");
		Path xml2 = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module/2/HSBC/US/WEB/VCH/vch_TEST_TestMoveMoney.xml");
		
		compareXMLStructure(xml1, xml2);
	}
	void compareXMLStructure(Path xmlFile1, Path xmlFile2){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc1 = db.parse(xmlFile1.toFile());
			logger.ln(XMLTools.inst().nodeToStr(doc1));
			
			Document doc2 = db.parse(xmlFile2.toFile());
			logger.ln(XMLTools.inst().nodeToStr(doc2));
		} catch (ParserConfigurationException
				| IOException | SAXException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	@Ignore
	@Test
	public void testXmlFilesEquality(){
		String xml1 = "data/vch_TEST_TestMoveMoney_1.xml";
		String xml2 = "data/vch_TEST_TestMoveMoney_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertTrue(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertTrue(equals);
	}
	
	@Test
	public void testSameStructWithDifferentAttributeOrder(){
		String xml1 = "data/same_with_different_AttributeOrder_1.xml";
		String xml2 = "data/same_with_different_AttributeOrder_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertTrue(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertTrue(equals);
	}

	
	
	@Ignore
	@Test
	public void testDeifferentByNodeName(){
		String xml1 = "data/different_by_nodeName_1.xml";
		String xml2 = "data/different_by_nodeName_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertFalse(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertFalse(equals);
	}
	@Ignore
	@Test
	public void testDeifferentByAttributeName(){
		String xml1 = "data/different_by_attributeNames_1.xml";
		String xml2 = "data/different_by_attributeNames_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertFalse(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertFalse(equals);
	}
	@Ignore
	@Test
	public void testDeifferentNodeOccurence(){
		String xml1 = "data/different_by_node_occurence_1.xml";
		String xml2 = "data/different_by_node_occurence_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertFalse(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertFalse(equals);
	}
	@Ignore
	@Test
	public void testDeifferentAttributeOccurence(){
		String xml1 = "data/different_by_attributeOccurence_1.xml";
		String xml2 = "data/different_by_attributeOccurence_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertFalse(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertFalse(equals);
	}
	@Ignore
	@Test
	public void testDeifferentNodeOrder(){
		String xml1 = "data/different_by_NodeOrder_1.xml";
		String xml2 = "data/different_by_NodeOrder_2.xml";
		
		boolean equals = comp.isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
		logger.log("Is %s and %s equal? %s", xml1, xml2, equals);
		Assert.assertFalse(equals);
		
		
		equals = comp.isXMLFilesEqual(xml2, xml1);
		logger.log("Is %s and %s equal? %s", xml2, xml1, equals);
		Assert.assertFalse(equals);
	}
	
	
	
	
	//===============================================================================
	// Business logic moved to XMLStructComparator
	//===============================================================================
//	
//	
//	boolean isXMLFilesEqual(String xml1, String xml2){
//		return isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
//	}
//	
//	boolean isXMLFilesEqual(Path xml1, Path xml2){
//		requireNonNull(xml1, xml2);
//		if(!(Files.exists(xml1) && Files.exists(xml2))){
//			logger.ln("One of the files doesn't esit: %s, %s", xml1, xml2);
//			return false;
//		}
//		
//		boolean retval = true;
//		Document dom1 = XMLTools.inst().fileToDom(xml1);
//		Document dom2 = XMLTools.inst().fileToDom(xml2);
//		if(!isDomEqual(dom1, dom2)){
//			return false;
//		}
//		
//		return retval;
//	}
//	
//	/**
//	 * Test <em>equality</em> of <code>doc1</code> and <code>doc2</code>. The <em>equality</em>
//	 * conforms with guidelines from July. 
//	 * <ul>
//	 * <li>For elements, compare only their node names and attribute, ignore TEXT_NODE value and 
//	 * attribute value</li>
//	 * </ul>
//	 * @param dom1
//	 * @param dom2
//	 * @return
//	 */
//	boolean isDomEqual(Document dom1, Document dom2){
//		boolean retval = true;
//		
//		if(null == dom1 && null == dom2){
//			return retval;
//		}else if(null != dom1 && null != dom2){
//			Node node1 = dom1.getDocumentElement();
//			Node node2 = dom2.getDocumentElement();
//			if(!isNodeEqual(node1, node2)){
//				return false;
//			}
//		}else{
//			logger.ln("One of the documents is null", XMLTools.inst().nodeToStr(dom1), XMLTools.inst().nodeToStr(dom2));
//			return false;
//		}
//		
//		return retval;
//	}
//	
//	
//	/**
//	 * Check equality of the two nodes (when their are ELEMENTs), the following items will be checked
//	 * <ul>
//	 * <li>node name</li>
//	 * <li>attribute names</li>
//	 * <li>child nodes</li>
//	 * </ul>
//	 * <p>
//	 * <strong>Note:</strong> siblings of this node will not be checked. It's the invoker's duty to check 
//	 * siblins of this node.
//	 * </p>
//	 * @param node1
//	 * @param node2
//	 * @return
//	 */
//	boolean isNodeEqual(Node node1, Node node2){
//		requireNonNull(node1, node2);
//		boolean retval = true;
//		
//		if(node1.getNodeType() != node2.getNodeType()){
//			logger.ln("Different node types, %s, %s", XMLTools.inst().nodeToStr(node1), XMLTools.inst().nodeToStr(node2));
//			return false;
//		}
//		
//		if(Node.ELEMENT_NODE == node1.getNodeType() 
////				|| Node.ATTRIBUTE_NODE == node1.getNodeType()
//				){
//			if(!node1.getNodeName().equals(node2.getNodeName())){
//				logger.ln("Different node names: %s, %s", node1.getNodeName(), node2.getNodeName());
//				return false;
//			}
//			if(!isNodeAttributesEqual(node1, node2)){
//				return false;
//			}
//			if(!isNodeChildrenEqual(node1, node2)){
//				return false;
//			}
////			if(!isNodeSiblingsEqual(node1, node2)){
////				return false;
////			}
//			//}
//		}
////		else if(Node.TEXT_NODE == node1.getNodeType()){
////			
////		}
//		return retval;
//	}
//	
//	boolean isNodeAttributesEqual(Node node1, Node node2){
//		requireNonNull(node1, node2);
//		boolean retval = true;
//		
//		NamedNodeMap attrs1 = node1.getAttributes();
//		NamedNodeMap attrs2 = node2.getAttributes();
////		if(null == attrs1 && null != attrs2
////			|| null != attrs1 && null == attrs2){
////			logger.ln("Node attributes not equals: %s, %s", XMLTools.inst().nodeToStr(node1), XMLTools.inst().nodeToStr(node2));
////			return false;
////		}
//		
//		Set<String> attrs1Name = extractNames(attrs1);
//		Set<String> attrs2Name = extractNames(attrs2);
//		if(!attrs1Name.equals(attrs2Name)){
//			logger.ln("Node attributes not equal: \n%s,\n%s", XMLTools.inst().nodeToStr(node1), XMLTools.inst().nodeToStr(node2));
//			return false;
//		}
//		
//		return retval;
//	}
//	
//	
//	Set<String> extractNames(NamedNodeMap namedNodeMap){
//		Set<String> retval = new HashSet<>();
//		if(null == namedNodeMap){
//			return retval;
//		}
//		for(int i = 0; i < namedNodeMap.getLength(); ++i){
//			retval.add(namedNodeMap.item(i).getNodeName());
//		}
//		return retval;
//	}
//	
//	
//	/**
//	 * Check equality of all siblings of these two nodes.
//	 * @param node1
//	 * @param node2
//	 * @return
//	 */
//	boolean isNodeSiblingsEqual(Node node1, Node node2){
//		requireNonNull(node1, node2);
//		boolean retval = true;
//		
//		Node node1Next = node1.getNextSibling();
//		Node node2Next = node2.getNextSibling();
//		
//		while(null != node1Next){
//			if(null == node2Next){
//				logger.ln("One of the nodes is null: %s, %s", XMLTools.inst().nodeToStr(node1Next), XMLTools.inst().nodeToStr(node2Next));
//				return false;
//			}
//			
//			if(!isNodeEqual(node1Next, node2Next)){
//				return false;
//			}
//			
//			node1Next = node1Next.getNextSibling();
//			node2Next = node2Next.getNextSibling();
//		}
//		
//		//null == node1Next
//		if(null != node2Next){
//			logger.ln("One of the nodes is null: %s, %s", XMLTools.inst().nodeToStr(node1Next), XMLTools.inst().nodeToStr(node2Next));
//			return false;
//		}
//		
////		return true;
//		return retval;
//	}
//	
//	
//	boolean isNodeChildrenEqual(Node node1, Node node2){
//		requireNonNull(node1, node2);
//		boolean retval = true;
//		
////		NodeList node1Children = node1.getChildNodes();
////		NodeList node2Children = node2.getChildNodes();
//		Node node1Child = node1.getFirstChild();
//		Node node2Child = node2.getFirstChild();
//		
////		while(null != node1Child){
////			if(null == node2)
////		}
//		
//		if(null == node1Child && null == node2Child){
//			return retval;
//		}else if(null != node1Child && null != node2Child){
//			if(!isNodeEqual(node1Child, node2Child)){
//				return false;
//			}
//			if(!isNodeSiblingsEqual(node1Child, node2Child)){
//				return false;
//			}
//		}else{
//			Node nonEmptyChild = Optional.ofNullable(node1Child).orElse(node2Child);
//			
//			//Ignore TEXT_NODE
//			if(Node.ELEMENT_NODE == nonEmptyChild.getNodeType()){
//				
//				logger.ln("One of the node is null: %s, %s", XMLTools.inst().nodeToStr(node1Child), XMLTools.inst().nodeToStr(node2Child));
//				return false;
//			}
//			
//			
//		}
//		
//		return retval;
//	}
//	
//	void requireNonNull(Object ... objects){
//		Arrays.stream(objects).forEach(Objects::requireNonNull);
//	}
//	
////	static final PrintStream out = System.out;
	private XMLStructComparator comp = XMLStructComparator.inst();
	private MsgLogger logger = MsgLogger.defaultLogger();
}
