/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Marta
 */
public class ClassXPATH {
    
    Document doc;
    
    
    //Método para abrir el archivo XML.
    public int abrirDOM(File fichero){
       
      
       try{
           DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); //Creamos un objeto DocumentBuilderFactory.
           factory.setIgnoringComments(true); //Ignoramos los comentarios que pueda contener el XML
           factory.setIgnoringElementContentWhitespace(true); //Ignoramos los espacios en blanco que tenga el documento.
           //Creamos un objeto DocumentBUilder para cargar en él la estructura de árbol del DOM.
           DocumentBuilder builder=factory.newDocumentBuilder();
           //Interpretamos (parseamos) el documento XML y generamos el DOM equivalente.
           doc=builder.parse(fichero);
           
           return 0;
    
       
       }catch(Exception e){
           e.printStackTrace();
           return -1;
       }        
    }
    
    
    //Método para ejecutar el XPATH.
    
    public String ejecutaXPATH(String entrada){
        String salida="";
        String[] datos_nodo=null;
        Node node;
        
        try{
          //Crea el objeto XPath
          XPath xpath = XPathFactory.newInstance().newXPath();
          //Crea un XPathExpression con la consulta deseada
          XPathExpression exp = xpath.compile(entrada);
          //Ejecuta la consulta indicando que se ejecute sobre el DOM y que devolverá
         //el resultado como una lista de nodos.
         Object result= exp.evaluate(doc, XPathConstants.NODESET);
         NodeList nodeList = (NodeList) result;
         
         //Ahora recorre la lista para sacar los resultados
          
         for(int i=0; i<nodeList.getLength();i++){
                   node=nodeList.item(i);
                   //Comprobamos los nodos guardados en la lista de nodos, en caso de que sea de tipo elemento, ejecuta.
                   if(node.getNodeType()==Node.ELEMENT_NODE && node.getNodeName()=="libro"){
                      //Llamamos al método procesar libro. Guardamos el contenido en el array.
                       datos_nodo=procesarLibro(node);
                       
                       salida=salida+"\n"+"Pubicado en;" + datos_nodo[0];
                       salida=salida+"\n"+"El autor es;" + datos_nodo[2];      
                       salida=salida+"\n"+"El titulo es;" + datos_nodo[1];
                       salida=salida+"\n"+"--------------------------";

                   }else if(node.getNodeType()==Node.ELEMENT_NODE && (node.getNodeName()=="titulo" || node.getNodeName()=="autor")){
                      salida = salida + "\n" + nodeList.item(i).getChildNodes().item(0).getNodeValue();
                   }else if(node.getNodeType()==Node.ATTRIBUTE_NODE && node.getNodeName()=="publicado"){
                      salida = salida + "\n" + nodeList.item(i).getChildNodes().item(0).getNodeValue();
                   }
            }
         
         
         
         
         /* for (int i = 0; i < nodeList.getLength(); i++) {
               salida = salida + "\n" + nodeList.item(i).getChildNodes().item(0).getNodeValue();
               
           }*/
   
        }catch(Exception ex){
            
           salida="Error: " + ex.toString();
        }
        
        return salida;
    }
    
    protected String[] procesarLibro(Node n){
        String datos[]= new String[3]; //Declaramos un array con tres posiciones para guardar los datos de los nodos hijo, que son tres.
        Node ntemp=null;
        int contador=1;
        //Obtenemos el primer valor y lo guardamos en el array.
        datos[0]=n.getAttributes().item(0).getNodeValue();
        NodeList nodos=n.getChildNodes(); //Creamos la lista para guardar los nodos texto de cada nodo elemento.
        
        for(int i=0; i<nodos.getLength(); i++){
            ntemp=nodos.item(i); //asignamos valor del elemento en posición i.
            
            if(ntemp.getNodeType()==Node.ELEMENT_NODE){
                 datos[contador]=ntemp.getChildNodes().item(0).getNodeValue(); //Obtenemos el valor del nodo texto y lo guardamos.
                 contador++;
            }
        }
        
        return datos;
    }
    
}
