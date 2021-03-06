/*
 * Copyright (C) 2008 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 *  	   Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.commons.converters;

import java.util.Vector;

import otsopack.commons.converters.impl.DefaultNTriplesUnionUtility;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFormatsManager;

// TODO: change this name
public class UnionManager {

	private static final Vector unionUtilities = new Vector();
	private static final SemanticFormatsManager formatsManager = new SemanticFormatsManager();
	
	static{
		addDefaultUnionUtilities();
	}
	
	private static void addDefaultUnionUtilities(){
		addUnionUtility(new DefaultNTriplesUnionUtility());
	}
	
	public static void addUnionUtility(IUnionUtility utility){
		unionUtilities.addElement(utility);
	}
	
	public static void reset(){
		unionUtilities.removeAllElements();
		addDefaultUnionUtilities();
	}
	
	private static boolean canConvertOutputGraph(IUnionUtility utility, SemanticFormat outputFormat){
		if(utility.isOutputSupported(outputFormat))
			return true;
		
		final SemanticFormat [] supportedOutputFormats = utility.getSupportedOutputFormats();
		for(int i = 0; i < supportedOutputFormats.length; ++i)
			if(formatsManager.canConvert(outputFormat, supportedOutputFormats[i]))
				return true;
		
		return false;
	}
	
	private static Graph convertToValidInputGraph(IUnionUtility unionUtility, Graph graph){
		if(unionUtility.isInputSupported(graph.getFormat()))
			return graph;
		
		final SemanticFormat [] supportedInputFormats = unionUtility.getSupportedInputFormats();
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(formatsManager.canConvert(graph.getFormat(), supportedInputFormats[i]))
				return formatsManager.convert(graph, supportedInputFormats[i]);
		
		return null;
	}
	
	private static boolean canConvertToValidInputGraph(IUnionUtility unionUtility, Graph graph){
		if(unionUtility.isInputSupported(graph.getFormat()))
			return true;
		
		final SemanticFormat [] supportedInputFormats = unionUtility.getSupportedInputFormats();
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(formatsManager.canConvert(graph.getFormat(), supportedInputFormats[i]))
				return true;
		
		return false;
	}
	
	public static Graph union(Graph [] graphs1, Graph [] graphs2, SemanticFormat outputFormat){
		return union(union(graphs1, outputFormat), union(graphs2, outputFormat), outputFormat);
	}
	
	public static Graph union(Graph [] graphs, SemanticFormat outputFormat){
		if(graphs.length == 0)
			throw new IllegalArgumentException("union requires at least one argument!");
		
		Graph current = graphs[0];
		for(int i = 1; i < graphs.length; ++i)
			current = union(current, graphs[i], outputFormat);
		return current;
	}
	
	public static Graph union(Graph graph1, Graph graph2, SemanticFormat outputFormat){
		for(int i = 0; i < unionUtilities.size(); ++i){
			final IUnionUtility unionUtility = (IUnionUtility)unionUtilities.elementAt(i);
			
			if(canConvertToValidInputGraph(unionUtility, graph1) 
					&& canConvertToValidInputGraph(unionUtility, graph2)
					&& canConvertOutputGraph(unionUtility, outputFormat)){
				final Graph convertedGraph1 = convertToValidInputGraph(unionUtility, graph1);
				final Graph convertedGraph2 = convertToValidInputGraph(unionUtility, graph2);
				
				if( unionUtility.isOutputSupported(outputFormat) ) {
					//if it can simply return in this format, why make it complex using external converters?
					return unionUtility.union(convertedGraph1, convertedGraph2, outputFormat);
				}
				// TODO check it
				final Graph resultingGraph = unionUtility.union(convertedGraph1, convertedGraph2);
				return formatsManager.convert(resultingGraph, outputFormat);
			}
		}
		
		throw new IllegalArgumentException("Can't convert other formats that ntriples at the moment ");
	}
}
