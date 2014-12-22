package com.rakesh.voicerecognitionexample;


public class MusicEntry {
	private long id;
	private String name;
	private String artist;
	private String pathWay;

	public MusicEntry()
	{

	}

	public MusicEntry(String name, String artist, String pathWay)
	{
		this.name = name;
		this.artist = artist;
		this.pathWay = pathWay;
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setpathWay(String pathWay) {
		this.pathWay = pathWay;
	}

	public boolean equals(Object otherDept)
	{
		return this.id == ((MusicEntry)otherDept).id;
	}

	// Will be used by the ArrayAdapter in the ListView

	public String toString() {
		return id + ": " + name + " - " + artist + " - " + pathWay;
	}

	public String getPathWay()
	{
		return pathWay;
	}
}

