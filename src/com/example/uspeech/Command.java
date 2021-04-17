package com.example.uspeech;

public class Command {
	
	// private variables
    public int _id;
    public String _pin;
    public String _commandOn;
    public String _commandOff;
	private String _command;
    
	// constructor
	public Command() {
    }
    
    
    
    // constructor
    public Command(int id, String pin, String commandOn, String commandOff) {
	this._id = id;
	this._pin = pin;
	this._commandOn = commandOn;
	this._commandOff = commandOff;
    }
    
    // constructor
    public Command(String pin, String commandOn, String commandOff) {
	this._pin = pin;
	this._commandOn = commandOn;
	this._commandOff = commandOff;
    }
    
    // constructor
    public Command(String pin, String command) {
		// TODO Auto-generated constructor stub
    	this._pin = pin;
    	this._command = command;    	
	}
	// getting ID
    public int getID() {
	return this._id;
    }
    
    // setting id
    public void setID(int id) {
	this._id = id;
    }

    // getting pin
    public String getPin() {
	return this._pin;
    }

    // setting pin
    public void setPin(String pin) {
	this._pin = pin;
    }
    
    // getting command
    public String getCommand() {
    	return this._command;
        }
    
    // setting command
    public void setCommand(String command) {
    	this._command = command;
        }
    
    // getting command on
    public String getCommandOn() {
	return this._commandOn;
    }

    // setting command on
    public void setCommandOn(String commandOn) {
	this._commandOn = commandOn;
    }

    // getting command off
    public String getCommandOff() {
	return this._commandOff;
    }

    // setting command off
    public void setCommandOff(String commandOff) {
	this._commandOff = commandOff;
    }

}
