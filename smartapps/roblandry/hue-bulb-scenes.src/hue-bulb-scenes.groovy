/**
 *  Hue/Bulb Scenes
 *
 *  Author: Rob Landry
 *  
 *  Version: 1.0.4
 *  
 *  Date: 2016-08-03
 */
definition(
	name: "Hue/Bulb Scenes",
	namespace: "roblandry",
	author: "Rob Landry",
	description: "Sets the colors and brightness level of your Philips Hue lights and level of smart bulbs.",
	category: "Mode Magic",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/hue.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/hue@2x.png",
	iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Partner/hue@3x.png"
)

preferences {
	page(name: "mainPage", title: "Adjust the color of your Hue lights to match your mood.", install: true, uninstall: true)
	page(name: "timeIntervalInput", title: "Only during a certain time") {
		section {
			input "starting", "time", title: "Starting", required: false
			input "ending", "time", title: "Ending", required: false
		}
	}
}

def mainPage() {
	def routines = getRoutineNames()
	def modesList = getModeNames()
	def alarmModes = ["Away", "Stay", "Off"]

	dynamicPage(name: "mainPage") {
		def anythingSet = anythingSet()
		if (anythingSet) {
			section("Info") {
				paragraph "Author: \tRob Landry"
				paragraph "Version: \t1.0.4"
				paragraph "Date: \t8/03/2016"
			}
			section("Set the lighting mood when..."){
				ifSet "motion", "capability.motionSensor", title: "Motion Here", required: false, multiple: true
				ifSet "contact", "capability.contactSensor", title: "Contact Opens", required: false, multiple: true
				ifSet "contactClosed", "capability.contactSensor", title: "Contact Closes", required: false, multiple: true
				ifSet "acceleration", "capability.accelerationSensor", title: "Acceleration Detected", required: false, multiple: true
				ifSet "mySwitch", "capability.switch", title: "Switch Turned On", required: false, multiple: true
				ifSet "mySwitchOff", "capability.switch", title: "Switch Turned Off", required: false, multiple: true
				ifSet "arrivalPresence", "capability.presenceSensor", title: "Arrival Of", required: false, multiple: true
				ifSet "departurePresence", "capability.presenceSensor", title: "Departure Of", required: false, multiple: true
				ifSet "smoke", "capability.smokeDetector", title: "Smoke Detected", required: false, multiple: true
				ifSet "water", "capability.waterSensor", title: "Water Sensor Wet", required: false, multiple: true
				ifSet "button1", "capability.button", title: "Button Press", required:false, multiple:true //remove from production
				ifSet "triggerModes", "mode", title: "System Changes Mode", required: false, multiple: true
				ifSet "timeOfDay", "time", title: "At a Scheduled Time", required: false
			}
		}
		section(anythingSet ? "Select additional mood lighting triggers" : "Set the lighting mood when...", hideable: anythingSet, hidden: true){
			ifUnset "motion", "capability.motionSensor", title: "Motion Here", required: false, multiple: true
			ifUnset "contact", "capability.contactSensor", title: "Contact Opens", required: false, multiple: true
			ifUnset "contactClosed", "capability.contactSensor", title: "Contact Closes", required: false, multiple: true
			ifUnset "acceleration", "capability.accelerationSensor", title: "Acceleration Detected", required: false, multiple: true
			ifUnset "mySwitch", "capability.switch", title: "Switch Turned On", required: false, multiple: true
			ifUnset "mySwitchOff", "capability.switch", title: "Switch Turned Off", required: false, multiple: true
			ifUnset "arrivalPresence", "capability.presenceSensor", title: "Arrival Of", required: false, multiple: true
			ifUnset "departurePresence", "capability.presenceSensor", title: "Departure Of", required: false, multiple: true
			ifUnset "smoke", "capability.smokeDetector", title: "Smoke Detected", required: false, multiple: true
			ifUnset "water", "capability.waterSensor", title: "Water Sensor Wet", required: false, multiple: true
			ifUnset "button1", "capability.button", title: "Button Press", required:false, multiple:true //remove from production
			ifUnset "triggerModes", "mode", title: "System Changes Mode", description: "Select mode(s)", required: false, multiple: true
			ifUnset "timeOfDay", "time", title: "At a Scheduled Time", required: false
		}

		// Hue Bulbs //
		section("Color Settings for Hue Bulbs...") {
			input "hues", "capability.colorControl", title: "Which Hue Bulbs?", required:false, multiple:true
			input "color", "enum", title: "Hue Color?", required: false, multiple:false, options: [
				"Relax":"Relax",
				"Read":"Read",
				"Concentrate":"Concentrate",
				"Energize":"Energize",
				"Bright":"Bright",
				"Dimmed":"Dimmed",
				"Night Light":"Night Light",
				"Red":"Red",
				"Orange":"Orange",
				"Yellow":"Yellow",
				"Green":"Green",
				"Blue":"Blue",
				"Purple":"Purple",
				"Pink":"Pink"]
			input "lightLevel", "enum", title: "Light Level?", required: false, options: [10:"10%",20:"20%",30:"30%",40:"40%",50:"50%",60:"60%",70:"70%",80:"80%",90:"90%",100:"100%"]
		}

		// Smart Bulbs //
		section("Brightness Settings for Smart Bulbs...") {
			input "bulbs", "capability.switchLevel", title: "Which Smart Bulbs?", required:false, multiple:true
			input "bulbLevel", "enum", title: "Light Level?", required: false, options: [10:"10%",20:"20%",30:"30%",40:"40%",50:"50%",60:"60%",70:"70%",80:"80%",90:"90%",100:"100%"]
		}

		// Turn On //
		section("Turn on these Lights/Switches...") {
			input "switchOn", "capability.switch", title: "Which Lights/Switches?", required:false, multiple:true
		}

		// Turn Off //
		section("Turn off these Lights/Switches...") {
			input "switchOff", "capability.switch", title: "Which Lights/Switches?", required:false, multiple:true
		}

		// Smart Home //
		section("Set these Actions...") {
			input "alarmTo", "enum", title:"Set Smart Home Monitor to", options:alarmModes, required:false
			input "modeTo", "enum", title: "Mode to change to:", options:modesList, multiple: false, required: false
			input "routineTo", "enum", title:"Execute routine", options:routines ,multiple: false, required:false
		}

		// More Options //
		section("More options", hideable: true, hidden: true) {
			input "frequency", "decimal", title: "Minimum time between actions (defaults to every event)", description: "Minutes", required: false
			href "timeIntervalInput", title: "Only during a certain time", description: timeLabel ?: "Tap to set", state: timeLabel ? "complete" : "incomplete"
			input "days", "enum", title: "Only on certain days of the week", multiple: true, required: false,
				options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
			input "modes", "mode", title: "Only when mode is:", multiple: true, required: false
			input "oncePerDay", "bool", title: "Only once per day", required: false, defaultValue: false
		}

		section([mobileOnly:true]) {
			label title: "Assign a name", required: false
			mode title: "Set for specific mode(s)", required: false
		}
	}
}
private anythingSet() {
	for (name in ["motion","contact","contactClosed","acceleration","mySwitch","mySwitchOff","arrivalPresence","departurePresence","smoke","water","button1","triggerModes","timeOfDay"]) {
		if (settings[name]) {
			return true
		}
	}
	return false
}

private ifUnset(Map options, String name, String capability) {
	if (!settings[name]) {
		input(options, name, capability)
	}
}

private ifSet(Map options, String name, String capability) {
	if (settings[name]) {
		input(options, name, capability)
	}
}

private def getRoutineNames() {
	def routines = location.helloHome?.getPhrases().collect() { it.label }
	return routines.sort()
}

private def getModeNames() {
	def modeTo = location.modes?.collect() { it.name }
	return modeTo.sort()
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribeToEvents()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	unschedule()
	subscribeToEvents()
}

def subscribeToEvents() {
	subscribe(app, appTouchHandler)
	subscribe(contact, "contact.open", eventHandler)
	subscribe(contactClosed, "contact.closed", eventHandler)
	subscribe(acceleration, "acceleration.active", eventHandler)
	subscribe(motion, "motion.active", eventHandler)
	subscribe(mySwitch, "switch.on", eventHandler)
	subscribe(mySwitchOff, "switch.off", eventHandler)
	subscribe(arrivalPresence, "presence.present", eventHandler)
	subscribe(departurePresence, "presence.not present", eventHandler)
	subscribe(smoke, "smoke.detected", eventHandler)
	subscribe(smoke, "smoke.tested", eventHandler)
	subscribe(smoke, "carbonMonoxide.detected", eventHandler)
	subscribe(water, "water.wet", eventHandler)
	subscribe(button1, "button.pushed", eventHandler)

	if (triggerModes) {
		subscribe(location, modeChangeHandler)
	}

	if (timeOfDay) {
		schedule(timeOfDay, scheduledTimeHandler)
	}
}

def eventHandler(evt) {
	log.trace "eventHandler($evt.name: $evt.value)"
	if (allOk) {
		log.trace "allOk"
		def lastTime = state[frequencyKey(evt)]
		if (oncePerDayOk(lastTime)) {
			if (frequency) {
				if (lastTime == null || now() - lastTime >= frequency * 60000) {
					takeAction(evt)
				}
			}
			else {
				takeAction(evt)
			}
		}
		else {
			log.debug "Not taking action because it was already taken today"
		}
	}
}

def modeChangeHandler(evt) {
	log.trace "modeChangeHandler $evt.name: $evt.value ($triggerModes)"
	if (evt.value in triggerModes) {
		eventHandler(evt)
	}
}

def scheduledTimeHandler() {
	log.trace "scheduledTimeHandler()"
	eventHandler(null)
}

def appTouchHandler(evt) {
	takeAction(evt)
}

private takeAction(evt) {

	if (frequency) {
		state[frequencyKey(evt)] = now()
	}

	state.previous = [:]

	//*****Hue
	def hueColor = 13
	def saturation = 56
	def hueLevel
	//h, s, l
	//Relax 12,78 57
	//Read 13, 48, 100
	//Concentrate 60, 6, 100
	//Energize 63,30, 100
	//Bright 13, 56, 100
	//Dimmed 13, 56, 30
	//Night Light 13, 56, 1

	switch(color) {
		case "Relax":
			hueColor = 12
			saturation = 78
			hueLevel = 57
			break;
		case "Read":
			hueColor = 13
			saturation = 48
			hueLevel = 100
			break;
		case "Concentrate":
			hueColor = 60
			saturation = 6
			hueLevel = 100
			break;
		case "Energize":
			hueColor = 63
			saturation = 30
			hueLevel = 100
			break;
		case "Bright":
			hueColor = 13
			saturation = 56
			hueLevel = 100
			break;
		case "Dimmed":
			hueColor = 13
			saturation = 56
			hueLevel = 30
			break;
		case "Night Light":
			hueColor = 13
			saturation = 56
			hueLevel = 1
			break;
		case "Red":
			hueColor = 100
			break;
		case "Orange":
			hueColor = 10
			break;
		case "Yellow":
			hueColor = 25
			break;
		case "Green":
			hueColor = 39
			break;
		case "Blue":
			hueColor = 70
			break;
		case "Purple":
			hueColor = 75
			break;
		case "Pink":
			hueColor = 83
			break;
	}

	if (lightLevel) {
		hueLevel = lightLevel as Integer
	} else if (!hueLevel) {
		hueLevel = 100
	}

	//log.debug "hueLevel: $hueLevel"
	hues.each {
		state.previous[it.id] = [
			"name": it.displayName,
			"level" : it.currentValue("level"),
			"hue": it.currentValue("hue"),
			"saturation": it.currentValue("saturation")
		]
	}

	def newValue = [hue: hueColor, saturation: saturation, level: hueLevel]
	//log.debug "Hue Current Values = $state.previous, Hue New Values = $newValue"

	hues*.setColor(newValue)

	//******SmartBulbs
	bulbs.each {
		state.previous[it.id] = [
			"switch": it.currentValue("switch"),
			"level" : it.currentValue("level")
		]
	}

	//log.debug "smart bulb current values = $state.previous"

	def bulbNewValue = [level: bulbLevel as Integer ?: 100]

	//log.debug "smart bulb New value = $bulbNewValue.level"

	bulbs*.setLevel(bulbNewValue.level)

	//*****SwitchOn
	switchOn.each {
		state.previous[it.id] = [
			"switch": it.currentValue("switch")
		]
	}

	//log.debug "switchOn current values = $state.previous"
	switchOn*.on()

	//*****SwitchOff
	switchOff.each {
		state.previous[it.id] = [
			"switch": it.currentValue("switch")
		]
	}

	//log.debug "switchOff current values = $state.previous"
	switchOff*.off()

	//Change Mode to
	if (modeTo) {
		setMode(modeTo)
	}

	//Change Smart home monitor to
	if (alarmTo) {
		setAlarmMode(alarmTo)
	}

	//Change Routine to
	if (routineTo) {
		setRoutine(routineTo)
	}
}

private frequencyKey(evt) {
	"lastActionTimeStamp"
}

private dayString(Date date) {
	def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	df.format(date)
}

private oncePerDayOk(Long lastTime) {
	def result = true
	if (oncePerDay) {
		result = lastTime ? dayString(new Date()) != dayString(new Date(lastTime)) : true
		log.trace "oncePerDayOk = $result"
	}
	result
}

// TODO - centralize somehow
private getAllOk() {
	modeOk && daysOk && timeOk
}

private getModeOk() {
	def result = !modes || modes.contains(location.mode)
	log.trace "modeOk = $result"
	result
}

private getDaysOk() {
	def result = true
	if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	log.trace "daysOk = $result"
	result
}

private getTimeOk() {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	log.trace "timeOk = $result"
	result
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private timeIntervalLabel()
{
	(starting && ending) ? hhmm(starting) + "-" + hhmm(ending, "h:mm a z") : ""
}
// TODO - End Centralize

private def setMode(name) {
	if(location.mode != name){
		setLocationMode(name)
		//log.debug "Mode changed to $name"
	}
}

private def setAlarmMode(name) {
	log.trace "Setting alarm system mode to \'${name}\'"

	def event = [
		name:		"alarmSystemStatus",
		value:		name,
		isStateChange:  true,
		displayed:	true,
		description:	"alarm system status is ${name}",
	]

	sendLocationEvent(event)
}

private def setRoutine(name) {
	log.trace "Executing Routine \'${name}\'"
	location.helloHome.execute(name)
}