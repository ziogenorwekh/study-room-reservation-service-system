package com.choongang.studyreservesystem.exception;

public class UserDuplicationException extends StudyReserveSytemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3100L;
	
	public UserDuplicationException() {
		super();
	}

	public UserDuplicationException(String message) {
		super(message);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
