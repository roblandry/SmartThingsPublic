/**
 *  Web Control
 *  Author: Rob Landry
 *
 *  Forked from:
 *  ThingLayer API access
 *
 *  Author: Jody Albritton
 *
 *
 * TODO: Powers, Thermostat
 *
 */


// Automatically generated. Make future change here.
import groovy.json.JsonBuilder
definition(
	name: "Web Control",
	namespace: "roblandry",
	author: "Rob Landry",
	description: "Connect all of the things. ",
	category: "My Apps",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
	oauth: [displayName: "Web Control", displayLink: ""])

preferences {
	section("Allow Endpoint to Control These Things...") {
		input "hubs", "capability.hub", title: "Which Hubs?", multiple: true, required: false
		input "switches", "capability.switch", title: "Which Switches?", multiple: true, required: false
		input "dimmers", "capability.switchLevel", title: "Which Dimmers?", multiple: true, required: false
		input "thermostats", "capability.thermostat", title: "Which Thermostats?", multiple: true, required: false 
		input "motions", "capability.motionSensor", title: "Which Motions?", multiple: true, required: false
		input "accelerations", "capability.accelerationSensor", title: "Which Accelerations?", multiple: true, required: false
		input "contacts", "capability.contactSensor", title: "Which Contacts?", multiple: true, required: false
		input "illuminants", "capability.illuminanceMeasurement", title: "Which Illuminance Sensors?", multiple: true, required: false
		input "temperatures", "capability.temperatureMeasurement", title: "Which Temperatures?", multiple: true, required: false 
		input "humidities", "capability.relativeHumidityMeasurement", title: "Which Humidities?", multiple: true, required: false 
		input "presence", "capability.presenceSensor", title: "Which Presence?", multiple: true, required: false 
		input "lock", "capability.lock", title: "Which Locks?", multiple: true, required: false
		input "batteries", "capability.battery", title: "Which Batteries?", multiple: true, required: false
		input "powers", "capability.powerMeter", title: "Power Meters", required:false, multiple: true
		input "energys", "capability.energyMeter", title: "Energy Meters", required:false, multiple: true 
		input "cameras", "capability.videoCamera", title: "Cameras", required:false, multiple: true 
	}
}


def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(hubs, "hub", handleHubEvent)
	subscribe(switches, "switch", handleSwitchEvent)
	subscribe(dimmers, "level", handleSwitchLevelEvent)
	subscribe(motions, "motion", handleMotionEvent)
	subscribe(accelerations, "acceleration", handleAccelerationEvent)
	subscribe(contacts, "contact", handleContactEvent)
	subscribe(illuminants, "illuminance", handleIlluminanceEvent)
	subscribe(temperatures, "temperature", handleTemperatureEvent)
	subscribe(humidities, "humidity", handleHumidityEvent)
	subscribe(lock, "lock", handleDoorLockEvent)
	subscribe(batteries, "battery", handleBatteryEvent)
	subscribe(powers, "power", handlePowerEvent)
	subscribe(energys, "energy", handleEnergyEvent)
	subscribe(presence, "presence", handlePresenceEvent)
	subscribe(cameras, "clip", handleCameraEvent)
}

def handleHubEvent(evt) {
	logField(evt) { it.toString() }
}

def handleIlluminanceEvent(evt) {
	logField(evt) { it.toString() }
}

def handleHumidityEvent(evt) {
	logField(evt) { it.toString() }
}

def handleTemperatureEvent(evt) {
	logField(evt) { it.toString() }
}

def handleContactEvent(evt) {
	logField(evt) { it == "open" ? "1" : "0" }
}

def handleAccelerationEvent(evt) {
	logField(evt) { it == "active" ? "1" : "0" }
}

def handleMotionEvent(evt) {
	logField(evt) { it == "active" ? "1" : "0" }
}

def handleSwitchEvent(evt) {
	logField(evt) { it.toString() }
}

def handleSwitchLevelEvent(evt) {
	logField(evt) { it.toString() }
}

def handleDoorLockEvent(evt) {
	logField(evt) {it == "locked" ? "locked" : "unlocked" }
}

def handleBatteryEvent(evt) {
	logField(evt) { it.toString() }
}

def handlePowerEvent(evt) {
	logField(evt) { it.toString() }
}

def handleEnergyEvent(evt) {
	logField(evt) { it.toString() }
}

def handlePresenceEvent(evt) {
	logField(evt) { it.toString() }
}

def handleCameraEvent(evt) {
	logField(evt) { it.toString() }
}

mappings {

	path("/all") {
		action: [
			GET: "listAll"
		]
	}
	path("/hubs") {
		action: [
			GET: "listHubs"
		]
	}
	path("/hubs/:id") {
		action: [
			GET: "showHub"
		]
	}
	path("/switches") {
		action: [
			GET: "listSwitches"
		]
	}
	path("/switches/:id") {
		action: [
			GET: "showSwitch"
		]
	}
	path("/switches/:id/:command") {
		action: [
			GET: "updateSwitch"
		]
	}
	path("/switches/:id/:events") {
		action: [
			GET: "showSwitchEvents"
		]
	}
	path("/locks") {
		action: [
			GET: "listLocks"
		]
	}
	path("/locks/:id") {
		action: [
			GET: "showLock"
		]
	}
	path("/locks/:id/:command") {
		action: [
			GET: "updateLock"
		]
	}
	path("/dimmers") {
		action: [
			GET: "listDimmers"
		]
	}
	path("/dimmers/:id") {
		action: [
			GET: "showDimmer"
		]
	}
	path("/dimmers/:id/:command") {
		action: [
			GET: "updateDimmer"
		]
	}
	path("/switches/:id/:command/:level") {
		action: [
			GET: "updateSwitch"
		]
	}
	path("/motions") {
		action: [
			GET: "listMotions"
		]
	}
	path("/motions/:id") {
		action: [
			GET: "showMotion"
		]
	}
	path("/motions/:id/events") {
		action: [
			GET: "showMotionEvents"
		]
	}
	path("/illuminants") {
		action: [
			GET: "listIlluminants"
		]
	}
	path("/illuminants/:id") {
		action: [
			GET: "showIlluminant"
		]
	}
	path("/contacts") {
		action: [
			GET: "listContacts"
		]
	}
	path("/contacts/:id") {
		action: [
			GET: "showContact"
		]
	}
	path("/temperatures") {
		action: [
			GET: "listTemperatures"
		]
	}
	path("/temperatures/:id") {
		action: [
			GET: "showTemperature"
		]
	}
	path("/temperatures/:id/:command") {
		action: [
			GET: "updateTemperatures"
		]
	}
	path("/humidities") {
		action: [
			GET: "listHumidities"
		]
	}
	path("/humidities/:id") {
		action: [
			GET: "showHumidity"
		]
	}
	path("/batteries") {
		action: [
			GET: "listBatteries"
		]
	}
	path("/batteries/:id") {
		action: [
			GET: "showBattery"
		]
	}
	path("/powers") {
		action: [
			GET: "listPowers"
		]
	}
	path("/powers/:id") {
		action: [
			GET: "showPower"
		]
	}
	path("/energies") {
		action: [
			GET: "listEnergies"
		]
	}
	path("/energies/:id") {
		action: [
			GET: "showEnergy"
		]
	}
	path("/thermostats") {
		action: [
			GET: "listThermostats"
		]
	}
	path("/thermostats/:id") {
		action: [
			GET: "showThermostat"
		]
	}
	path("/thermostats/:id/:command/:temp") {
		action: [
			GET: "updateThermostat"
		]
	}
	path("/presence") {
		action: [
			GET: "listPresence"
		]
	}
	path("/presences/:id") {
		action: [
			GET: "showPresence"
		]
	}
	path("/cameras") {
		action: [
			GET: "listCameras"
		]
	}
	path("/cameras/:id") {
		action: [
			GET: "showCamera"
		]
	}
	path("/cameras/:id/:events") {
		action: [
			GET: "showCameraEvents"
		]
	}
}

//All
def listAll() {
	def resp
	resp = switches?.collect{[type: "switch", id: it.id, name: it.displayName, status: it.currentValue('switch')]}?.sort{it.name}
	resp += dimmers?.collect{[type: "dimmer", id: it.id, name: it.displayName, level: it.currentValue('level')]}?.sort{it.name}
	resp += lock?.collect{[type: "lock", id: it.id, name: it.displayName, status: it.currentValue('lock')]}?.sort{it.name}
	resp += temperatures?.collect{[type: "temperatureMeasurement", id: it.id, name: it.displayName, status: it.currentValue('temperature')]}?.sort{it.name}
	resp += humidities?.collect{[type: "relativeHumidityMeasurement", id: it.id, name: it.displayName, status: it.currentValue('humidity')]}?.sort{it.name}
	resp += presence?.collect{[type: "presence", id: it.id, name: it.displayName, status: it.currentValue('presence')]}?.sort{it.name}
	resp += motions?.collect{[type: "motion", id: it.id, name: it.displayName, status: it.currentValue('motion')]}?.sort{it.name}
	resp += illuminants?.collect{[type: "illuminant", id: it.id, name: it.displayName, status: it.currentValue('illuminance')]}?.sort{it.name}
	resp += contacts?.collect{[type: "contact", id: it.id, name: it.displayName, status: it.currentValue('contact')]}?.sort{it.name}
	resp += batteries?.collect{[type: "battery", id: it.id, name: it.displayName, status: it.currentValue('battery')]}?.sort{it.name}
	//resp << powers?.collect{[type: "power", id: it.id, name: it.displayName, status: it.currentValue("power")]}?.sort{it.name}
	resp += energys?.collect{[type: "energy", id: it.id, name: it.displayName, status: it.currentValue("energy")]}?.sort{it.name}
	resp += thermostats.collect{device(it,"thermostat")}
	resp += cameras?.collect{[type: "clip", id: it.id, name: it.displayName, status: it.currentValue('clip')]}?.sort{it.name}
	resp += hubs?.collect{[type: "hub", id: it.id, name: it.displayName, status: it.currentValue('status')]}?.sort{it.name}

	resp.removeAll(Collections.singleton(null))
	return resp
}

//hubs
def listHubs() {
	def hub = location.hubs[0]
	hubs:{[path: "hubs",  type: "hub", id: hub.id, name: hub.name]}
	//hubs?.collect{[path: "hubs", type: "hub", id: it.id, name: it.name]}?.sort{it.name}
}
def showHub() {
	show(hubs, "hub")
	//hubs:{[path: "hubs",  type: "hub", id: hub.id, name: hub.name ]}
	//[id: device.hub.id, label: device.hub.name]

}
def showHubEvents() {
	getEvents(hubs, "hub")
}

//switches
def listSwitches() {
	switches: switches?.collect{[path: "switches", type: "switch", id: it.id, name: it.displayName, status: it.currentValue('switch')]}?.sort{it.name}
}


def showSwitch() {
	show(switches, "switch")
}

def showSwitchEvents() {
	getEvents(switches, "switch")
}

void updateSwitch() {
	update(switches)
}

//dimmers
def listDimmers() {
	dimmers?.collect{[path: "dimmers", type: "dimmer", id: it.id, name: it.displayName, level: it.currentValue('level')]}?.sort{it.name}
}
def showDimmer() {
	show(dimmers, "level")
}
void updateDimmer() {
	update(dimmers)
}

//locks

def listLocks() {
	lock?.collect{[path: "locks", type: "lock", id: it.id, name: it.displayName, status: it.currentValue('lock')]}?.sort{it.name}
}

def showLock() {
	show(lock, "lock")
}

void updateLock() {
	update(lock)
}

def listTemperatures() {
	temperatures?.collect{[path: "temperatures", type: "temperatureMeasurement", id: it.id, name: it.displayName, status: it.currentValue('temperature')]}?.sort{it.name}
}

def showTemperature() {
	show(temperatures, "temperature")
}

def listHumidities() {
	humidities?.collect{[path: "humidities", type: "relativeHumidityMeasurement", id: it.id, name: it.displayName, status: it.currentValue('humidity')]}?.sort{it.name}
}

def showHumidity() {
	show(humidities, "humidity")
}

def listPresence() {
	presence?.collect{[path: "presence", type: "presence", id: it.id, name: it.displayName, status: it.currentValue('presence')]}?.sort{it.name}
}

def showPresence() {
	show(presence, "presence")
}

def listMotions() {
	motions?.collect{[path: "motions", type: "motion", id: it.id, name: it.displayName, status: it.currentValue('motion')]}?.sort{it.name}
}

def showMotion() {
	show(motions, "motion")
}

def showMotionEvents() {
	getEvents(motions, "motion")
}

def listIlluminants() {
	illuminants?.collect{[path: "illuminants", type: "illuminant", id: it.id, name: it.displayName, status: it.currentValue('illuminance')]}?.sort{it.name}
}

def showIlluminant() {
	show(illuminants, "illuminance")
}

def listContacts() {
	contacts?.collect{[path: "contacts", type: "contact", id: it.id, name: it.displayName, status: it.currentValue('contact')]}?.sort{it.name}
}

def showContact() {
	show(contacts, "contact")
}

def listBatteries() {
	batteries?.collect{[path: "batteries", type: "battery", id: it.id, name: it.displayName, status: it.currentValue('battery')]}?.sort{it.name}
}

def showBattery() {
	show(batteries, "battery")
}

def listPowers() {
	powers?.collect{[path: "powers", type: "power", id: it.id, name: it.displayName, status: it.currentValue("power")]}?.sort{it.name}
}

def showPower() {
	show(powers, "power")
}

def listEnergies() {
	energys?.collect{[path: "energies", type: "energy", id: it.id, name: it.displayName, status: it.currentValue("energy")]}?.sort{it.name}
}

def showEnergy() {
	show(energys, "energy")
}

//thermostats
def listThermostats() {
	thermostats.collect{device(it,"thermostat")}
}

def showThermostat() {
	show(thermostats, "thermostat")
}

void updateThermostat() {

	def device = thermostats.find { it.id == params.id }
	def command = params.command
	def temp = params.temp

	log.debug "$command ${params.id} at $temp"

	if(command == 'heat')
	{
		device.setHeatingSetpoint(temp)
	}
	else if(command == 'cool')
	{
		device.setCoolingSetpoint(temp)	
	}
}

def listCameras() {
	cameras?.collect{[path: "cameras", type: "clip", id: it.id, name: it.displayName, status: it.currentValue('clip')]}?.sort{it.name}
}

def showCamera() {
	show(cameras, "clip")
}

def showCameraEvents() {
	getEvents(cameras, "clip")
}

def deviceHandler(evt) {}

private void update(devices) {
	log.debug "update, request: params: ${params}, devices: $devices.id"

	//def command = request.JSON?.command
	def command = params.command
	def level = params.level
	//let's create a toggle option here
	if (command) {
		def device = devices.find { it.id == params.id }
		if (!device) {
			httpError(404, "Device not found")
		} else {
			if(command == "toggle")
			{
				if(device.currentValue('switch') == "on")
					device.off();
				else
					device.on();
			}
			else if(command == "level")
			{
				device.setLevel(level.toInteger())
			}
			else if(command == "events")
			{
				device.events(max: 20)
			}
		
			else
			{
				device."$command"()
			}
		}
	}
}

private show(devices, type) {
	log.debug("show: ${devices} ${type}")
	def device = devices.find { it.id == params.id }
	if (!device) {
		httpError(404, "Device not found")
	} else {
		def attributeName = type == "motionSensor" ? "motion" : type
		def s = device.currentState(attributeName)
		[id: device.id, label: device.displayName, value: s?.value, unitTime: s?.date?.time, type: type]
	}
}

private getEvents(devices, type) {
	def device = devices.find { it.id == params.id }
	def events
	def result
	if (!device) {
		httpError(404, "Device not found")
	} else {
		if (type == "clip") {
			events = device.events(source:true, max:100000)
			//events = device.events(source:true, max:all)
			events = events.findAll{it.name == "clip"}
			events = events.findAll{it.value == "completed"}
			result = events.collect{[id: device.id, label: device.displayName, value: it?.value, unixTime: it?.date?.time, name: it.name, data: it.data]}
		} else {
			events = device.events(max: 40)
			events = events.findAll{it.name == type}
			result = events.collect{item(device, it)}
		}

		result
	}	
}

private item(device, s) {
	device && s ? [uid: s.id, device_id: device.id, label: device.displayName, name: s.name, value: s.value, date: s.date] : null
}

private device(it, type) {
	it ? [id: it.id, label: it.label, type: type] : null
}

private logField(evt, Closure c) {
	//httpPostJson(uri: "https://YOUR_FIREBASE_INSTANCE.firebaseio.com/events/${evt.deviceId}/${evt.name}.json",   body:[device: evt.deviceId, name: evt.name, value: evt.value, date: evt.isoDate, unit: evt.unit]) {
	//	log.debug evt.name+" Event data successfully posted"
	//}
}