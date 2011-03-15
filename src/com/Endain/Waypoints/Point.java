package com.Endain.Waypoints;

public class Point
{
	private int id;
	private String world;
	private int x;
	private int y;
	private int z;
	
	public Point(int id, String world, int x, int y, int z)
	{
		this.id = id;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int getZ()
	{
		return this.z;
	}
	
	public String getWorld()
	{
		return this.world;
	}
}
