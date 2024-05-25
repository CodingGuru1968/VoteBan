package com.codingguru.voteban.scheduler;

public enum VoteResultCalculatorType {

	MIN_VOTES,
	SERVER_PERCENTAGE;

	public static VoteResultCalculatorType getVoteCalculatorType(String name) {
		for (VoteResultCalculatorType type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return MIN_VOTES;
	}

	public boolean isSuccessful(VoteType voteType, int votes, int playersOnline) {
		switch (this) {
		case MIN_VOTES:
			return votes >= voteType.getMinVotes();
		case SERVER_PERCENTAGE:
			int percentage = calculatePercentage(votes, playersOnline);
			return percentage >= voteType.getMinVotes();
		}
		return false;
	}

	public int calculatePercentage(double obtained, double total) {
		return (int) Math.round(obtained * 100 / total);
	}

}