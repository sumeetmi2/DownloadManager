/*
 * 
 * Created on Oct 19, 2016
 *
 */
package com.downloadmanager.objects;

/**
 * @author SumeetS
 *
 */
public class ChunkWrapper {
    private String id;
    private String byteRange;
    
    public ChunkWrapper(String id,String byteRange){
	this.id=id;
	this.byteRange = byteRange;
    }
    
    public String getId(){
	return id;
    }
    public String getByteRange(){
	return byteRange;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ChunkWrapper){
            ChunkWrapper o = (ChunkWrapper) obj;
            return this.id == o.id;
        }
	return false;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Integer.parseInt(id.replaceAll((id.split("-[0-9]*$")[0]), "").replaceAll("-", ""));
    }
}
