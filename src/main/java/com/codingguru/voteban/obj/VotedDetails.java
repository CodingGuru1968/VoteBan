package com.codingguru.voteban.obj;

import com.codingguru.voteban.scheduler.VoteType;

public class VotedDetails {

	private final long endTime;
	private final VoteType voteType;

	public VotedDetails(long endTime, VoteType voteType) {
		this.endTime = endTime;
		this.voteType = voteType;
	}

	public VoteType getVoteType() {
		return voteType;
	}

	public long getEndTime() {
		return endTime;
	}

}
