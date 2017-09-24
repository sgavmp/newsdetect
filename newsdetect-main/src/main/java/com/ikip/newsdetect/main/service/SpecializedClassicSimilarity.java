/**
 * 
 */
package com.ikip.newsdetect.main.service;

import es.ucm.visavet.gbf.app.LanguageLoad;
import org.apache.commons.jexl3.*;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.eclipse.jetty.io.RuntimeIOException;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class SpecializedClassicSimilarity extends ClassicSimilarity {

	
	private JexlExpression coordFor=null;
	private JexlExpression queryNormFor=null;
	private JexlExpression tfFor=null;
	private JexlExpression idFor=null;
	private JexlExpression generalFor=null;
	private JexlEngine jexl;
	private boolean original;
	private static String discoverS;
	private static String originalS;
	private static String generalForS;
	private static String idForS;
	private static String tfForS;
	private static String queryNormForS;
	private static String coordForS;
	private static boolean loaded=false;
	
	/**
	 * 
	 */
	public SpecializedClassicSimilarity() {
		super();
		Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("math", Math.class);
        
		jexl = new JexlBuilder().cache(512).namespaces(funcs).strict(true).silent(false).create();
		
	//	super.setDiscountOverlaps(false);
		original=true;
	}
	
	@Override
	public float coord(int overlap, int maxOverlap) {
		
		float salida = super.coord(overlap, maxOverlap);
		
		if (coordFor!=null)
		{
			try {
				 JexlContext context = new MapContext();
				    
				    context.set("overlap".toLowerCase(),(float)overlap );
				    context.set("maxOverlap".toLowerCase(),(float)maxOverlap );
				    context.set("coord".toLowerCase(),salida );
				    
				  Number result = (Number) coordFor.evaluate(context);
				  return result.floatValue();
			} catch (Exception e) {
				e.printStackTrace();
				return 0f;
			}
		
		}
		else
			return salida;
	}

	
	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		
		float salida = super.queryNorm(sumOfSquaredWeights);
				
		if (queryNormFor!=null)
		{
			try {
				 JexlContext context = new MapContext();
				    
				    context.set("sumOfSquaredWeights".toLowerCase(),sumOfSquaredWeights );
				    context.set("queryNorm".toLowerCase(),salida );
				    
				  Number result = (Number) queryNormFor.evaluate(context);
				  return result.floatValue();
			} catch (Exception e) {
				e.printStackTrace();
				return 0f;
			}
		
		}
		else
			return salida; 
	}
	
	@Override
	public float tf(float freq) {
		
		float salida = super.tf(freq);
		
		if (tfFor!=null)
		{
			try {
				 JexlContext context = new MapContext();
				    
				    context.set("freq".toLowerCase(),freq );
				    context.set("tf".toLowerCase(),salida );
				    
				  Number result = (Number) tfFor.evaluate(context);
				  return result.floatValue();
			} catch (Exception e) {
				e.printStackTrace();
				return 0f;
			}
		
		}
		else
			return salida;
	}
	
	@Override
	public float idf(long docFreq, long numDocs) {
		
		float salida =super.idf(docFreq, numDocs);
		
		if (idFor!=null)
		{
			try {
				 JexlContext context = new MapContext();
				    
				    context.set("docFreq".toLowerCase(),(float)docFreq );
				    context.set("numDocs".toLowerCase(),(float)numDocs );
				    context.set("idf".toLowerCase(),salida );
				    
				  Number result = (Number) idFor.evaluate(context);
				  
				  return result.floatValue();
			} catch (Exception e) {
				e.printStackTrace();
				return 0f;
			}
		
		}
		else
			return salida;
	}

	/**
	 * @return the coordFor
	 */
	public JexlExpression getCoordFor() {
		return coordFor;
	}

	/**
	 * @param coordFor the coordFor to set
	 */
	public void setCoordFor(String coordForS) {
		JexlExpression coordForT = jexl.createExpression( coordForS.toLowerCase() );
		JexlContext context = new MapContext();
	    
	    context.set("overlap".toLowerCase(),1 );
	    context.set("maxOverlap".toLowerCase(),1 );
	    context.set("coord".toLowerCase(),1 );
	    
	    coordForT.evaluate(context);
	  
		this.coordFor = coordForT;
	}

	/**
	 * @return the queryNormFor
	 */
	public JexlExpression getQueryNormFor() {
		return queryNormFor;
	}

	/**
	 * @param queryNormFor the queryNormFor to set
	 */
	public void setQueryNormFor(String queryNormFor) {
		
		JexlExpression queryNormForT = jexl.createExpression( queryNormFor.toLowerCase() );
		JexlContext context = new MapContext();
	    
		 context.set("sumOfSquaredWeights".toLowerCase(),1 );
		 context.set("queryNorm".toLowerCase(),1 );
	    
	    queryNormForT.evaluate(context);
	  
		
		this.queryNormFor = queryNormForT;
	}

	/**
	 * @return the tfFor
	 */
	public JexlExpression getTfFor() {
		return tfFor;
	}

	/**
	 * @param tfFor the tfFor to set
	 */
	public void setTfFor(String tfFor) {
		
		JexlExpression setTfForT = jexl.createExpression(tfFor.toLowerCase());
		JexlContext context = new MapContext();
	    
		  context.set("freq".toLowerCase(),1 );
		  context.set("tf".toLowerCase(),1 );
	    
	    setTfForT.evaluate(context);
		
		this.tfFor = setTfForT;
	}

	/**
	 * @return the idFor
	 */
	public JexlExpression getIdFor() {
		return idFor;
	}

	/**
	 * @param idFor the idFor to set
	 */
	public void setIdFor(String idFor) {
		
		JexlExpression IdForT = jexl.createExpression(idFor.toLowerCase());
		JexlContext context = new MapContext();
	    
		context.set("docFreq".toLowerCase(),1 );
	    context.set("numDocs".toLowerCase(),1 );
	    context.set("idf".toLowerCase(),1 );
	    
	    IdForT.evaluate(context);
		
		this.idFor = IdForT;
	}

	public void loadfromfile(String path, String file) {
		
		File xmlFile = new File( path+file );
		if (xmlFile.exists())
		{
		
		if (!loaded)
		{
			System.out.println("Load:"+path+file);
		SAXBuilder builder = new SAXBuilder();
	    
	    try
	    {
	    	org.jdom2.Document document = (org.jdom2.Document) builder.build( xmlFile );
	    	org.jdom2.Element rootNode = document.getRootElement();
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "coordFor" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					coordForS=rootNodeHijo;
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "queryNormFor" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					queryNormForS=rootNodeHijo;
	    					
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "tfFor" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					tfForS=rootNodeHijo;
	    					
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "idFor" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					idForS=rootNodeHijo;
	    					
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "generalFor" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					generalForS=rootNodeHijo;
	    					
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "original" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					originalS=rootNodeHijo;
	    					
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    	
	    	{
	    		List<org.jdom2.Element> rootNodeHijoL =rootNode.getChildren( "discover" );
	    		if (!rootNodeHijoL.isEmpty())
	    			{
	    			try {
	    				String rootNodeHijo =rootNodeHijoL.get(0).getText();
	    				if (rootNodeHijo!=null)
	    					discoverS=rootNodeHijo;
	    					
					} catch (Exception e) {
						e.printStackTrace();
					}
	    			
	    			}
	    	}
	    
	    	
	    	loaded=true;
	   
	    
	    }catch ( IOException io ) {
	        System.out.println( io.getMessage() );
	    }catch ( JDOMException jdomex ) {
	        System.out.println( jdomex.getMessage() );
	    }
		 }
		}else
			System.out.println( LanguageLoad.getinstance().find("web/lucene/errorloadnofile") +  path+file );
		
		try {
			if (coordForS!=null&&!coordForS.isEmpty())
				setCoordFor(coordForS);
		} catch (Exception e) {
			coordFor=null;
			e.printStackTrace();
		}
		
		try {
			if (queryNormForS!=null&&!queryNormForS.isEmpty())
			setQueryNormFor(queryNormForS);
		} catch (Exception e) {
			queryNormFor=null;
			e.printStackTrace();
		}
		
		try {
			if (tfForS!=null&&!tfForS.isEmpty())
			setTfFor(tfForS);
		} catch (Exception e) {
			tfFor=null;
			e.printStackTrace();
		}
		
		try {
			if (idForS!=null&&!idForS.isEmpty())
			setIdFor(idForS);
		} catch (Exception e) {
			idFor=null;
			e.printStackTrace();
		}
		
		try {
			if (generalForS!=null&&!generalForS.isEmpty())
			setGeneralForm(generalForS);
		} catch (Exception e) {
			generalFor=null;
			e.printStackTrace();
		}
		
		try {
			if (originalS!=null&&!originalS.isEmpty())
				original=Boolean.parseBoolean(originalS);
		} catch (Exception e) {
			original=true;
			e.printStackTrace();
		}
		
		try {
			if (discoverS!=null&&!discoverS.isEmpty())
				super.setDiscountOverlaps(Boolean.parseBoolean(discoverS));
		} catch (Exception e) {
			super.setDiscountOverlaps(true);
			e.printStackTrace();
		}
		
		
	}
	
	public void savetofile(String path, String file) {
		loaded=false;
		try {
			System.out.println("Save:"+path+file);
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         DOMImplementation implementation = builder.getDOMImplementation();
	         Document document = implementation.createDocument(null, "Symy", null);
	         document.setXmlVersion("1.0");
	         Element raiz = document.getDocumentElement();
	         
	         if (coordFor!=null)
	         {  
	         Element Node = document.createElement("coordFor"); 
	         Text NodeKeyValue = document.createTextNode(coordFor.getSourceText());
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         if (queryNormFor!=null)
	         {  
	         Element Node = document.createElement("queryNormFor"); 
	         Text NodeKeyValue = document.createTextNode(queryNormFor.getSourceText());
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         if (tfFor!=null)
	         {  
	         Element Node = document.createElement("tfFor"); 
	         Text NodeKeyValue = document.createTextNode(tfFor.getSourceText());
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         if (idFor!=null)
	         {  
	         Element Node = document.createElement("idFor"); 
	         Text NodeKeyValue = document.createTextNode(idFor.getSourceText());
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         if (generalFor!=null)
	         {  
	         Element Node = document.createElement("generalFor"); 
	         Text NodeKeyValue = document.createTextNode(generalFor.getSourceText());
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         {
	         Element Node = document.createElement("original"); 
	         Text NodeKeyValue = document.createTextNode(Boolean.toString(original));
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         {
	         Element Node = document.createElement("discover"); 
	         Text NodeKeyValue = document.createTextNode(Boolean.toString(getDiscountOverlaps()));
	         raiz.appendChild(Node);
	         Node.appendChild(NodeKeyValue);
	         }
	         
	         Source source = new DOMSource(document);
	            Result result = new StreamResult(new File(path+file));
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.transform(source, result);
	         
		} catch (Exception e) {
			throw new RuntimeIOException(LanguageLoad.getinstance().find("web/lucene/errorsave"));
		}
		
		// TODO Salva las cosas dede el XML
		
	}

	public void setGeneralForm(String formuLucidf) {
		JexlExpression generalForT = jexl.createExpression(formuLucidf.toLowerCase());
		JexlContext context = new MapContext();
	    
		  context.set("Wa".toLowerCase(),1 );
		    context.set("Wl".toLowerCase(),1 );
		    context.set("Ws".toLowerCase(),1 );
//		    context.set("Wst".toLowerCase(),Wsl );
		    context.set("Sl".toLowerCase(),1 );
	    
	    generalForT.evaluate(context);
		
		this.generalFor = generalForT;
		
	}

	public JexlExpression getGeneralFor() {
		if (generalFor==null)
			setGeneralForm("Sl+(Wa+1)+(Wl+1)+(Ws+1)");
	
		return generalFor;
	}

	@Override
	public float lengthNorm(FieldInvertState state) {
		
		if (original)
			return super.lengthNorm(state);
		else 
			return 1f;
	}

	public boolean isOriginal() {
		return original;
	}

	public void setOriginal(boolean original) {
		this.original = original;
	}
	
}
