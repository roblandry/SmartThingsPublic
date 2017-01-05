/**
 *  Color Light Scenes
 *
 *  Author: Rob Landry
 *  
 *  Version: 2.0
 *  
 *  Date: 2017-01-04
 */
definition(
	name: "Color Light Coordinator",
	namespace: "roblandry/Color Light Coordinator",
	author: "Rob Landry",
	description: "Sets the colors and brightness level of your Color lights and level of smart bulbs (this is the parent).",
	category: "Mode Magic",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/hue.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/hue@2x.png",
	iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Partner/hue@3x.png",
	singleInstance: true
)

preferences {
    // The parent app preferences are pretty simple: just use the app input for the child app.
    page(name: "mainPage", title: "Color Light Scenes", install: true, uninstall: true,submitOnChange: true) {
        section {
            app(name: "ColorLightScenes", appName: "Color Light Scenes", namespace: "roblandry/Color Light Scenes", title: "Create New Scene", multiple: true)
            }
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
    // nothing needed here, since the child apps will handle preferences/subscriptions
    // this just logs some messages for demo/information purposes
    log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
    }
}