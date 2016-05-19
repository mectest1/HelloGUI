package com.mec.app.plugin.filemanager.resources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * Compares only element/attribute names, ignoring node/attribute values;
 * <h4>There differences will be ignored</h4>
 * <ul>
 * <li>Text node value</li>
 * <li>Attribute values</li>
 * <li>Attribute order</li>
 * </ul>
 * <h4>These discrepancies will be detected </h4>
 * <ul>
 * <li>Node order</li>
 * <li>Node name</li>
 * <li>Note occurrence</li>
 * <li>Attribute occurrence</li>
// * <li></li>
 * </ul>
 * @author MEC
 *
 */
public class XMLStructComparator {
	
	private static final XMLStructComparator instance = new XMLStructComparator();
	private XMLStructComparator(){
		
	}
	public void setLogger(MsgLogger logger) {
		this.logger = logger;
	}
	public static XMLStructComparator inst(){
		return instance;
	}
	



	public boolean isXMLFilesEqual(String xml1, String xml2){
		return isXMLFilesEqual(Paths.get(xml1), Paths.get(xml2));
	}
	
	public boolean isXMLFilesEqual(Path xml1, Path xml2){
		requireNonNull(xml1, xml2);
		if(!(Files.exists(xml1) && Files.exists(xml2))){
			logger.ln("One of the files doesn't esit: %s, %s", xml1, xml2);
			return false;
		}
		
		boolean retval = true;
		Document dom1 = XMLTools.inst().fileToDom(xml1);
		Document dom2 = XMLTools.inst().fileToDom(xml2);
		if(!isDomEqual(dom1, dom2)){
			return false;
		}
		
		return retval;
	}
	
	/**
	 * Test <em>equality</em> of <code>doc1</code> and <code>doc2</code>. The <em>equality</em>
	 * conforms with guidelines from July. 
	 * <ul>
	 * <li>For elements, compare only their node names and attribute, ignore TEXT_NODE value and 
	 * attribute value</li>
	 * </ul>
	 * @param dom1
	 * @param dom2
	 * @return
	 */
	boolean isDomEqual(Document dom1, Document dom2){
		boolean retval = true;
		
		if(null == dom1 && null == dom2){
			return retval;
		}else if(null != dom1 && null != dom2){
			Node node1 = dom1.getDocumentElement();
			Node node2 = dom2.getDocumentElement();
			if(!isNodeEqual(node1, node2)){
				return false;
			}
		}else{
			logger.ln("One of the documents is null", XMLTools.inst().nodeToStr(dom1), XMLTools.inst().nodeToStr(dom2));
			return false;
		}
		
		return retval;
	}
	
	
	/**
	 * Check equality of the two nodes (when their are ELEMENTs), the following items will be checked
	 * <ul>
	 * <li>node name</li>
	 * <li>attribute names</li>
	 * <li>child nodes</li>
	 * </ul>
	 * <p>
	 * <strong>Note:</strong> siblings of this node will not be checked. It's the invoker's duty to check 
	 * siblins of this node.
	 * </p>
	 * @param node1
	 * @param node2
	 * @return
	 */
	boolean isNodeEqual(Node node1, Node node2){
		requireNonNull(node1, node2);
		boolean retval = true;
		
		if(node1.getNodeType() != node2.getNodeType()){
			logger.ln("Different node types, %s, %s", XMLTools.inst().nodeToStr(node1), XMLTools.inst().nodeToStr(node2));
			return false;
		}
		
		if(Node.ELEMENT_NODE == node1.getNodeType() 
//				|| Node.ATTRIBUTE_NODE == node1.getNodeType()
				){
			if(!node1.getNodeName().equals(node2.getNodeName())){
				logger.ln("Different node names: %s, %s", node1.getNodeName(), node2.getNodeName());
				return false;
			}
			if(!isNodeAttributesEqual(node1, node2)){
				return false;
			}
			if(!isNodeChildrenEqual(node1, node2)){
				return false;
			}
//			if(!isNodeSiblingsEqual(node1, node2)){
//				return false;
//			}
			//}
		}
//		else if(Node.TEXT_NODE == node1.getNodeType()){
//			
//		}
		return retval;
	}
	
	boolean isNodeAttributesEqual(Node node1, Node node2){
		requireNonNull(node1, node2);
		boolean retval = true;
		
		NamedNodeMap attrs1 = node1.getAttributes();
		NamedNodeMap attrs2 = node2.getAttributes();
//		if(null == attrs1 && null != attrs2
//			|| null != attrs1 && null == attrs2){
//			logger.ln("Node attributes not equals: %s, %s", XMLTools.inst().nodeToStr(node1), XMLTools.inst().nodeToStr(node2));
//			return false;
//		}
		
		Set<String> attrs1Name = extractNames(attrs1);
		Set<String> attrs2Name = extractNames(attrs2);
		if(!attrs1Name.equals(attrs2Name)){
			logger.ln("Node attributes not equal: \n%s,\n%s", XMLTools.inst().nodeToStr(node1), XMLTools.inst().nodeToStr(node2));
			return false;
		}
		
		return retval;
	}
	
	
	Set<String> extractNames(NamedNodeMap namedNodeMap){
		Set<String> retval = new HashSet<>();
		if(null == namedNodeMap){
			return retval;
		}
		for(int i = 0; i < namedNodeMap.getLength(); ++i){
			retval.add(namedNodeMap.item(i).getNodeName());
		}
		return retval;
	}
	
	
	/**
	 * Check equality of all siblings of these two nodes.
	 * @param node1
	 * @param node2
	 * @return
	 */
	boolean isNodeSiblingsEqual(Node node1, Node node2){
		requireNonNull(node1, node2);
		boolean retval = true;
		
		Node node1Next = node1.getNextSibling();
		Node node2Next = node2.getNextSibling();
		
		while(null != node1Next){
			if(null == node2Next){
				logger.ln("One of the nodes is null: %s, %s", XMLTools.inst().nodeToStr(node1Next), XMLTools.inst().nodeToStr(node2Next));
				return false;
			}
			
			if(!isNodeEqual(node1Next, node2Next)){
				return false;
			}
			
			node1Next = node1Next.getNextSibling();
			node2Next = node2Next.getNextSibling();
		}
		
		//null == node1Next
		if(null != node2Next){
			logger.ln("One of the nodes is null: %s, %s", XMLTools.inst().nodeToStr(node1Next), XMLTools.inst().nodeToStr(node2Next));
			return false;
		}
		
//		return true;
		return retval;
	}
	
	
	boolean isNodeChildrenEqual(Node node1, Node node2){
		requireNonNull(node1, node2);
		boolean retval = true;
		
//		NodeList node1Children = node1.getChildNodes();
//		NodeList node2Children = node2.getChildNodes();
		Node node1Child = node1.getFirstChild();
		Node node2Child = node2.getFirstChild();
		
//		while(null != node1Child){
//			if(null == node2)
//		}
		
		if(null == node1Child && null == node2Child){
			return retval;
		}else if(null != node1Child && null != node2Child){
			if(!isNodeEqual(node1Child, node2Child)){
				return false;
			}
			if(!isNodeSiblingsEqual(node1Child, node2Child)){
				return false;
			}
		}else{
			Node nonEmptyChild = Optional.ofNullable(node1Child).orElse(node2Child);
			
			//Ignore TEXT_NODE
			if(Node.ELEMENT_NODE == nonEmptyChild.getNodeType()){
				
				logger.ln("One of the node is null: %s, %s", XMLTools.inst().nodeToStr(node1Child), XMLTools.inst().nodeToStr(node2Child));
				return false;
			}
			
			
		}
		
		return retval;
	}
	
	void requireNonNull(Object ... objects){
		Arrays.stream(objects).forEach(Objects::requireNonNull);
	}
	
//	static final PrintStream out = System.out;
	private MsgLogger logger = MsgLogger.defaultLogger();
}
