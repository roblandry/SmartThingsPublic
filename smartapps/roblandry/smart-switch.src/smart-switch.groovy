/**
 *  Smart Switch
 *
 *  Version: 1.2
 *
 *  Copyright 2016 Rob Landry
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
	name:		"Smart Switch",
	namespace:	"roblandry",
	author:		"Rob Landry",
	description:	"Turn on/off switch with a time delay.",
	category:	"Mode Magic",
	iconUrl:	"https://s3.amazonaws.com/smartapp-icons/Convenience/App-BigButtonsAndSwitches.png",
   	iconX2Url:	"https://s3.amazonaws.com/smartapp-icons/Convenience/App-BigButtonsAndSwitches@2x.png",
	iconX3Url:	"https://s3.amazonaws.com/smartapp-icons/Convenience/App-BigButtonsAndSwitches@2x.png")


preferences {
	section("Info") {
		paragraph "Author:  Rob Landry"
		paragraph "Version: 1.2"
		paragraph "Date: 1/04/2017"
	}
	section("Devices") {
		input "switches", "capability.switch", title: "Switch to turn on/off", multiple: true
	}
	section("Preferences") {
		input "onOff", "bool", title: "Turn it ON or OFF", required: true, defaultValue: 1
		input "triggerModes", "mode", title: "System Changes Mode", required: false, multiple: true
   		input "triggerSwitch", "capability.switch", title: "Switch trigger", multiple: true
		input "delayMinutes", "number", title: "Minutes", required: false, defaultValue: 0
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
	unschedule ("turnOnOffAfterDelay")
	subscribe(app, appTouchHandler)
	subscribe(switches, "switch", switchHandler)
   	subscribe(triggerSwitch, "switch", triggerSwitchHandler)
	subscribe(location, modeChangeHandler)
}

def modeChangeHandler(evt) {
	//log.debug "Mode Change Handler: ${evt.name}: ${evt.value}, ($triggerModes)"
	log.trace "modeChangeHandler $evt.name: $evt.value ($triggerModes)"
	if (evt.value in triggerModes) {
		eventHandler(evt)
	}
}

def appTouchHandler(evt) {
	log.debug "Unschedule turnoffafterdelay"
	unschedule("turnOnOffAfterDelay")
	eventHandler(evt)
}

def triggerSwitchHandler(evt) {
	//log.debug "Trigger Switch Handler: ${evt.name}: ${evt.value}"
	log.trace "triggerSwitchHandler $evt.name: $evt.value"
	if (evt.value == "on") {
		eventHandler(evt)
	}
}

def eventHandler(evt) {
	//log.debug "Event Handler: ${evt.name}: ${evt.value}, State: ${state}"
	state.startTimer = now()
	if(delayMinutes) {
		// This should replace any existing off schedule
		unschedule("turnOnOffAfterDelay")
		runIn(delayMinutes*60, "turnOnOffAfterDelay", [overwrite: false])
		def turnOnOff = (onOff ? "on" : "off")
		//sendNotificationEvent("${switches} will turn  ${turnOnOff} in ${delayMinutes} minute(s).")
		log.debug "${switches} will turn  ${turnOnOff} in ${delayMinutes} minute(s)."
	} else {
		turnOnOffAfterDelay()
	}
}


def switchHandler(evt) {
	//log.debug "Switch Handler: ${evt.name}: ${evt.value}, State: ${state}"
	if (evt.value == "off") {
		log.info "Turning off."
	} else if (evt.value == "on") {
		log.info "Turning on."
	}

}

def turnOnOffAfterDelay() {
	log.debug "turnOnOffAfterDelay: State: ${state}"

	if (state.startTimer) {
		def elapsed = now() - state.startTimer
		if (elapsed >= (delayMinutes ?: 0) * 60000L) {
			//sendNotificationEvent("${switches} timer is up.")
			if (onOff) {
				switches.on()
			} else {
				switches.off()
			}
		} else {
			//def turnOnOff = (onOff ? "on" : "off")
			//sendNotificationEvent("Failed to turn ${turnOnOff} ${switches} in ${delayMinutes} minute(s), restarting timer for 1 minute.")
			unschedule("turnOnOffAfterDelay")
			runIn(60, "turnOnOffAfterDelay", [overwrite: false])
		}
	}
}