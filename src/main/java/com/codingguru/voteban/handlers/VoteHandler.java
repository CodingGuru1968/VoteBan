package com.codingguru.voteban.handlers;

public class VoteHandler {

	private final static VoteHandler INSTANCE = new VoteHandler();
	private boolean isChatDisabled;

	public VoteHandler() {
		this.isChatDisabled = false;
	}

	public void setChatDisabled(boolean isChatDisabled) {
		this.isChatDisabled = isChatDisabled;
	}

	public boolean isChatDisabled() {
		return isChatDisabled;
	}

	public static VoteHandler getInstance() {
		return INSTANCE;
	}

}