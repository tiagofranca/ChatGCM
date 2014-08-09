/**
 * 
 */
package com.projeto.gcmserver.graphdb;

import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.INDEX_BACKEND_KEY;
import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY;

import java.io.File;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author tfr_souza
 *
 */
public class SimpleBobbyGraphDBFactory {
	
	private static TitanGraph bobbyGraph;
	public static final String INDEX_NAME = "search";
	
	private SimpleBobbyGraphDBFactory() {}
	
	public static TitanGraph getInstance() {
//		String directory = System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "bobby";
		String directory = System.getProperty("user.home") + File.separator + "tmp" + File.separator + "bobby";
		if (bobbyGraph == null) {
			bobbyGraph = TitanFactory.open(directory);
			if (bobbyGraph == null) {
				BaseConfiguration config = new BaseConfiguration();
		        Configuration storage = config.subset(GraphDatabaseConfiguration.STORAGE_NAMESPACE);
		        // configuring local backend
		        storage.setProperty(GraphDatabaseConfiguration.STORAGE_BACKEND_KEY, "local");
		        storage.setProperty(GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY, directory);
		        // configuring elastic search index
		        Configuration index = storage.subset(GraphDatabaseConfiguration.INDEX_NAMESPACE).subset(INDEX_NAME);
		        index.setProperty(INDEX_BACKEND_KEY, "elasticsearch");
		        index.setProperty("local-mode", true);
		        index.setProperty("client-only", false);
		        index.setProperty(STORAGE_DIRECTORY_KEY, directory + File.separator + "es");
		
		        bobbyGraph = TitanFactory.open(config);
		        load(bobbyGraph);
			}
	        return bobbyGraph;
		} else {
			return bobbyGraph;
		}
	}
	
	private static void load(TitanGraph graph) {
				
        graph.makeType().name("phonenumber").dataType(Long.class).indexed(INDEX_NAME, Vertex.class).unique(Direction.BOTH).makePropertyKey();
        graph.makeType().name("name").dataType(String.class).unique(Direction.OUT).makePropertyKey();
        graph.makeType().name("pass").dataType(String.class).unique(Direction.OUT).makePropertyKey();
        graph.makeType().name("gcmkey").dataType(String.class).unique(Direction.OUT).makePropertyKey();

        graph.makeType().name("knows").unique(Direction.OUT).makeEdgeLabel();
        graph.makeType().name("suggested").unique(Direction.OUT).makeEdgeLabel();

        graph.commit();
        
	}

}
