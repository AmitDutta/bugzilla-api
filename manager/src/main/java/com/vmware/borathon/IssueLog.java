package com.vmware.borathon;

import java.io.InputStream;

public class IssueLog {
	public IssueLog(String path, InputStream stream) {
		this.path = path;
		this.stream = stream;
	}
	
	public String getPath() {
		return path;
	}
	
	public InputStream getStream() {
		return stream;
	}

	private String path;
	private InputStream stream;
}
