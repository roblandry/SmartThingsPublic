/**
 *  Smart Bathroom Timer
 *
 *  Version: 2.0
 *
 *  Copyright 2016 Rob Landry
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
	name:		"Smart Bathroom Timer",
	namespace:	"roblandry",
	author:		"Rob Landry",
	description:	"Turn on bathroom switch with motion and then off with a time delay based on various triggers.",
	category:	"Mode Magic",
	iconUrl:	"https://s3.amazonaws.com/smartapp-icons/Convenience/App-BigButtonsAndSwitches.png",
   	iconX2Url:	"https://s3.amazonaws.com/smartapp-icons/Convenience/App-BigButtonsAndSwitches@2x.png",
	iconX3Url:	"https://s3.amazonaws.com/smartapp-icons/Convenience/App-BigButtonsAndSwitches@2x.png")


preferences {
	section("Info") {
		paragraph "Author:  Rob Landry"
		paragraph "Version: 2.0"
		paragraph "Date:    10/15/2016"
	}

	section("Common Preferences") {
		input "switches", "capability.switch", title: "Switch to turn on/off", required: true, multiple: false
		input "delayMinutes", "number", title: "Switch On, App Touch Minutes", required: false, defaultValue: 0
	}

	section("Motion Trigger") {
		input "motions", "capability.motionSensor", title: "Motion Sensor to trigger action", multiple: false
		input "delayMinutesM", "number", title: "Motion Minutes", required: false, defaultValue: 0
	}

	section("Mode Trigger") {
		input "triggerModes", "mode", title: "System Changes Mode", required: false, multiple: true
		input "delayMinutesD", "number", title: "Mode Minutes", required: false, defaultValue: 0
	}

	section("Switch Trigger 1") {
		input "trigger1", "capability.switch", title: "Switch1 to trigger action", required: false, multiple: false
		input "delayMinutes1", "number", title: "Trigger1 Minutes", required: false, defaultValue: 0
	}

	section("Switch Trigger 2") {
		input "trigger2", "capability.switch", title: "Switch2 to trigger action", required: false, multiple: false
		input "delayMinutes2", "number", title: "Trigger1 Minutes", required: false, defaultValue: 0
	}

	section("Switch Trigger 3") {
		input "trigger3", "capability.switch", title: "Switch3 to trigger action", required: false, multiple: false
		input "delayMinutes3", "number", title: "Trigger3 Minutes", required: false, defaultValue: 0
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	unschedule ("turnOffAfterDelay")
	state.trigger=""
	state.timer=0
	subscribe(app, appTouchHandler)
	subscribe(switches, "switch.on", switchOnHandler)
	subscribe(switches, "switch.off", switchOffHandler)
	subscribe(trigger1, "switch.on", triggerHandler1)
	subscribe(trigger2, "switch.on", triggerHandler2)
	subscribe(trigger3, "switch.on", triggerHandler3)
	subscribe(motions, "motion.active", motionActiveHandler)
	subscribe(motions, "motion.inactive", motionInactiveHandler)
	subscribe(location, modeChangeHandler)
}

def modeChangeHandler(evt) {
	//log.debug "Mode Change Handler: ${evt.name}: ${evt.value}, ($triggerModes)"
	// If mode changes, set trigger mode
	log.info "Mode Change Timer Activated."
	if (evt.value in triggerModes) {
		state.trigger="mode"
		state.delay=delayMinutes
		state.timer=0
		eventHandler(evt)
	}
}

def appTouchHandler(evt) {
	//log.debug "App Touch Handler"
	// If app button pressed, set trigger mode
	log.info "App Touch Timer Activated."
	state.trigger="app"
	state.delay=delayMinutes
	state.timer=0
	eventHandler(evt)
}

def triggerHandler1(evt) {
	//log.debug "Trigger1 Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// If trigger used, set trigger mode
	log.info "Trigger1 Timer Activated."
	state.trigger="trigger1"
	state.delay=delayMinutes1
	state.timer=0
	eventHandler(evt)
}

def triggerHandler2(evt) {
	//log.debug "Trigger2 Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// If trigger used, set trigger mode
	log.info "Trigger2 Timer Activated."
	state.trigger="trigger2"
	state.delay=delayMinutes2
	state.timer=0
	eventHandler(evt)
}

def triggerHandler3(evt) {
	//log.debug "Trigger3 Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// If trigger used, set trigger mode
	log.info "Trigger3 Timer Activated."
	state.trigger="trigger3"
	state.delay=delayMinutes3
	state.timer=0
	eventHandler(evt)
}

def motionActiveHandler(evt) {
	//log.debug "Motion Active Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// If motion active and no trigger already set, set trigger mode (this only turns on lights)
	log.info "Motion Detected."
	if (state.trigger == "") {
		state.trigger="motionActive"
	}
	eventHandler(evt)
}

def motionInactiveHandler(evt) {
	//log.debug "Motion Inactive Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// Set to motion event if motion stops, and no previous trigger set, or reset motion timer
	if ((state.trigger == "") || (state.trigger == "motionActive")){
		log.info "Motion Timer Activated."
		state.trigger="motionInactive"
		state.delay=delayMinutesM
		state.timer=0
	}
	eventHandler(evt)
}

def switchOnHandler(evt) {
	//log.debug "Switch On Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// If switch turned on, and no other triggers, set trigger
	if (state.trigger == "") {
		log.info "Switch On Timer Activated."
		state.trigger="switchOn"
		state.delay=delayMinutes
		state.timer=0
		eventHandler(evt)
	}
	log.info "Turning on."
}

def switchOffHandler(evt) {
	//log.debug "Switch Off Handler: ${evt.name}: ${evt.value}, State: ${state}"
	// If switch off, reset defaults
	log.info "Turning off."
        unschedule("turnOffAfterDelay")
	state.trigger=""
	state.timer=0
}

def eventHandler(evt) {
	//log.debug "Event Handler: ${evt.name}: ${evt.value}, State: ${state}"

	// Determine if light is off, and if so, turn on 
	// unless due to motion inactive or switchOn (prevents forever loop)
	if ((state.trigger != "motionInactive") && (state.trigger != "switchOn")) {
		// Get current switch state (on=1, off=0)
		def switchState = switches.currentValue("switch")

		// If switch is off, turn it on
		if (switchState == "off") {
			//log.debug "${state.trigger} turned switches on"
			switches.on()		
		}
	}

	if ((state.trigger != "motionActive") && !(state.timer)) {
		// Set timer unless 0, then turn off
		if(state.delay) {
			state.timer = 1
			// This should replace any existing schedule
			log.info "Setting Timer for ${state.delay} Minute(s)."
			unschedule("turnOffAfterDelay")
			runIn(state.delay*60, "turnOffAfterDelay", [overwrite: false])
		} else {
			turnOffAfterDelay()
		}
	}
}

def turnOffAfterDelay() {
	//log.debug "turnOffAfterDelay: State: ${state}"

	// Get the status of motion
	def motionState = motions.currentState("motion")

	// If there is motion, reset the timer based on motion
	if (motionState.value == "active") {
		//log.debug "Changing from ${state.trigger} to Motion"
		log.info "Times up, but Motion Detected. Leaving lights on."
		state.trigger="motionActive"
		state.timer=0
	// Otherwise no motion, turn lights off
	} else {
		//log.debug "${state.trigger} Timer turned switches off"
		switches.off()
	}
}