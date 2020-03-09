package com.perago.techtest;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DiffRendererImpl implements DiffRenderer {
	
	private String lineSeparator = System.getProperty("line.separator");
	private static String depthString;
	private static String currentDepthString;
	
	{
		depthString = "";
		currentDepthString = "";
	}

	public String render(Diff<?> diff) throws DiffException {

		Class originalClass;
		originalClass = (diff.getHolder() ==  null) ? null : diff.getHolder().getClass();
		List<Diff.ChangeLog> changeLogs = diff.getChangeLogs();
		StringBuilder builder = new StringBuilder();
		
		int noOfChanges = changeLogs.size();
		Diff.ChangeLog changeLog = null;
		
		Iterator<String> collectionIterator;
		
		for (int eachChangeIndex = 0; eachChangeIndex < noOfChanges; eachChangeIndex++) {
			changeLog = changeLogs.get(eachChangeIndex);
			
			builder.append(getDepthString(changeLog.getDepth()));
			indent(builder, changeLog.getDepth());
			
			if (originalClass == null) {
				builder.append(changeLog.getStatus()).append(":").append(changeLog.getFieldName());
				builder.append(lineSeparator);
			} else if (changeLog.getFieldName().equals(originalClass.getSimpleName()) && changeLog.isParent()) {
				builder.append(changeLog.getStatus()).append(":").append(originalClass.getSimpleName());
				builder.append(lineSeparator);
			} else {
				try {
					if (changeLog.getType().equals(originalClass.getSimpleName()) && changeLog.isParent()) {
						builder.append(changeLog.getStatus()).append(":").append((changeLog.getFieldName()));
						builder.append(lineSeparator);
					} else if (changeLog.getStatus().equals(Status.Update)) {
						
						builder.append(changeLog.getStatus()).append(":").append((changeLog.getFieldName()));
						
						if(changeLog.getOldValue() != null || changeLog.getValue() != null) {
							builder.append(" from ");
							if(changeLog.getOldValue() != null)
							{
								if(changeLog.getOldValue() instanceof Collection)
								{
									builder.append(getCollectionContent((Collection)changeLog.getOldValue()));
								}
								else
								{
									builder.append("\"").append(changeLog.getOldValue()).append("\"");
								}
							}
							else
							{
								builder.append(changeLog.getOldValue());
							}
							
							builder.append(" to ");
							
							if(changeLog.getValue() != null)
							{
								if(changeLog.getValue() instanceof Collection)
								{
									builder.append(getCollectionContent((Collection)changeLog.getValue()));
								}
								else
								{
									builder.append("\"").append(changeLog.getValue()).append("\"");
								}
							}
							else
							{
								builder.append(changeLog.getValue());
							}
						}
						
						builder.append(lineSeparator);
					} else if (changeLog.getStatus().equals(Status.Delete)) {
						builder.append(changeLog.getStatus()).append(":").append((changeLog.getFieldName()));
						builder.append(lineSeparator);
					} else {
						builder.append(changeLog.getStatus()).append(":").append((changeLog.getFieldName()))
								.append(" as ");
						if(changeLog.getValue() != null)
						{
							builder.append("\"").append(changeLog.getValue()).append("\"");
						}
						else
						{
							builder.append(changeLog.getValue());
						}
						builder.append(lineSeparator);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(eachChangeIndex < noOfChanges - 1) {
				
			}
		}

		return builder.toString();
	}

	private Object getCollectionContent(Collection collectionObject) {
		Iterator collectionIterator = collectionObject.iterator();
		StringBuilder builder = new StringBuilder("");
		builder.append("{");
		while(collectionIterator.hasNext())
		{
			builder.append("\"").append(collectionIterator.next()).append("\"");
			if(collectionIterator.hasNext()) builder.append(",");
		}
		builder.append("}");
		
		return builder;
	}

	private static String getDepthString(int depth) {
		try
		{
			if("".equalsIgnoreCase(currentDepthString))
			{
				depthString = "" + depth;
			}
			else 
			{
				int currentDepthLevel = -1;
				if(!currentDepthString.contains("."))
				{
					currentDepthLevel = 1;
				}
				else 
				{
					if(currentDepthString.length() == (2 * depth - 1))
					{
						if(depth * (depth - 1) < currentDepthString.length())
						{
							currentDepthLevel = Integer.valueOf("" + currentDepthString.charAt(depth * (depth - 1)));
						}
						else
						{
							currentDepthLevel = Integer.valueOf("" + currentDepthString.charAt(2 * (depth - 1)));
						}
						currentDepthString = currentDepthString.substring(0, currentDepthString.length() - 2);
						currentDepthLevel ++;
					}
					else if(currentDepthString.length() > (2 * depth - 1))
					{
						currentDepthString = currentDepthString.substring(0, depth + 1);
						currentDepthLevel = Integer.valueOf("" + currentDepthString.charAt(depth * (depth - 1)));
						currentDepthString = currentDepthString.substring(0, currentDepthString.length() - 2);
						currentDepthLevel ++;
					}
					else if(currentDepthString.length() < (2 * depth - 1))
					{
						currentDepthLevel = 1;
					}
				}
				depthString = currentDepthString + "." + currentDepthLevel;
			}
			
			currentDepthString = depthString;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		
		return depthString;
	}

	private static void indent(StringBuilder builder, int depth) {
		for (int j = 0; j < depth; j++) {
			builder.append(" ");
		}
	}

}
