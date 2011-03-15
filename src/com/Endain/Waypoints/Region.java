package com.Endain.Waypoints;

public class Region
{
	private int centerX;
	private int centerY;
	private int centerZ;
	@SuppressWarnings("unused")
	private int protectRadius;
	private int protectUpperX;
	private int protectUpperZ;
	private int protectLowerX;
	private int protectLowerZ;
	@SuppressWarnings("unused")
	private int saveRadius;
	private int saveUpperX;
	private int saveUpperZ;
	private int saveLowerX;
	private int saveLowerZ;
	private Point owner;
	
	public Region(int x, int y, int z, int protectRadius, int saveRadius, Point owner)
	{
		this.centerX = x;
		this.centerY = y;
		this.centerZ = z;
		this.protectRadius = protectRadius;
		this.protectUpperX = x + protectRadius;
		this.protectUpperZ = z + protectRadius;
		this.protectLowerX = x - protectRadius;
		this.protectLowerZ = z - protectRadius;
		this.saveRadius = saveRadius;
		this.saveUpperX = x + saveRadius;
		this.saveUpperZ = z + saveRadius;
		this.saveLowerX = x - saveRadius;
		this.saveLowerZ = z - saveRadius;
		this.owner = owner;
	}
	
	public Point getOwner()
	{
		return this.owner;
	}
	
	public boolean isSaved(int x, int z)
	{
		if(x <= this.saveUpperX && x >= this.saveLowerX)
			if(z <= this.saveUpperZ && z >= this.saveLowerZ)
				return true;
		return false;
	}
	
	public boolean isProtected(int x, int z)
	{
		if(x <= this.protectUpperX && x >= this.protectLowerX)
			if(z <= this.protectUpperZ && z >= this.protectLowerZ)
				return true;
		return false;
	}
	
	public int getCenterX()
	{
		return this.centerX;
	}
	
	public int getCenterY()
	{
		return this.centerY;
	}
	
	public int getCenterZ()
	{
		return this.centerZ;
	}
}
