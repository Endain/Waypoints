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
	private int protectUpperY;
	private int protectLowerX;
	private int protectLowerZ;
	private int protectLowerY;
	@SuppressWarnings("unused")
	private int saveRadius;
	private int saveUpperX;
	private int saveUpperZ;
	private int saveUpperY;
	private int saveLowerX;
	private int saveLowerZ;
	private int saveLowerY;
	private Point owner;
	
	public Region(int x, int y, int z, int protectRadius, int saveRadius, Point owner)
	{
		this.centerX = x;
		this.centerY = y;
		this.centerZ = z;
		this.protectRadius = protectRadius;
		this.protectUpperX = x + protectRadius;
		this.protectUpperZ = z + protectRadius;
		this.protectUpperY = y + protectRadius;
		this.protectLowerX = x - protectRadius;
		this.protectLowerZ = z - protectRadius;
		this.protectLowerY = y - protectRadius;
		this.saveRadius = saveRadius;
		this.saveUpperX = x + saveRadius;
		this.saveUpperZ = z + saveRadius;
		this.saveUpperY = y + saveRadius;
		this.saveLowerX = x - saveRadius;
		this.saveLowerZ = z - saveRadius;
		this.saveLowerY = y - saveRadius;
		this.owner = owner;
	}
	
	public Point getOwner()
	{
		return this.owner;
	}
	
	public boolean isSaved(int x, int y, int z)
	{
		if(x <= this.saveUpperX && x >= this.saveLowerX)
			if(z <= this.saveUpperZ && z >= this.saveLowerZ)
				if(y <= this.saveUpperY && y >= this.saveLowerY)
					return true;
		return false;
	}
	
	public boolean isProtected(int x, int y, int z)
	{
		if(x <= this.protectUpperX && x >= this.protectLowerX)
			if(z <= this.protectUpperZ && z >= this.protectLowerZ)
				if(y <= this.protectUpperY && y >= this.protectLowerY)
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
