# hassalarm
Android app for integration with Hass.io / Home Assistant as an `input_datetime` for the next scheduled alarm on the device.

Expect that alarm clocks schedule alarms properly which will trigger the system wide `ACTION_NEXT_ALARM_CLOCK_CHANGED`.
Once that happen, a call to your Hass.io instance will happen within an hour, given that there is an Internet connection. On failure, the Android OS will retry later.

## Home Assistant setup
1. Add a `input_datetime` with both date and time in your `configuration.xml`
  ```
  input_datetime:
    next_alarm:
      name: Next scheduled alarm
      has_date: true
      has_time: true
  ```
1. Add a time sensor in your `configuration.xml`:
  ```
  sensor:
  - platform: time_date
    display_options:
      - 'date_time'
  ```
1. If you want the value to persist on Home Assistant restarts, enable the [History](https://www.home-assistant.io/integrations/history/) and [Recorder](https://www.home-assistant.io/integrations/recorder) components.
1. Add some automation for your new input:
  ```yaml
  automation:
    trigger:
      platform: template
      value_template: "{{ states('sensor.date_time') == (state_attr('input_datetime.next_alarm', 'timestamp') | int | timestamp_custom('%Y-%m-%d, %H:%M', True)) }}"
    action:
      service: light.turn_on
      entity_id: light.bedroom
  ```

Or if you want to trigger an automation five minutes before the alarm will go off:
```yaml
  automation:
    trigger:
      platform: template
      value_template: "{{ ((as_timestamp(states('sensor.date_time').replace(',','')) | int) + 5*60) == (state_attr('input_datetime.next_alarm', 'timestamp') | int)  }}"

    action:
      service: light.turn_on
      entity_id: light.bedroom
```


## App usage
1. Install via [Google Play Store](https://play.google.com/store/apps/details?id=com.fjun.hassalarm) or clone the repo and build the app: `./gradlew installDebug`
1. Create a [long lived token](https://www.home-assistant.io/docs/authentication/#your-account-profile) on your profile in Home Assistant.
1. Open the app and setup your hostname, longed live token and input_datetime entity ID: `input_datetime.next_alarm`
1. Schedule an alarm in any of your alarm apps

Once your device have a network connection, it should eventually do a call to the Hass.io API and your input_datetime should be set.

## Build status
[![Build Status](https://travis-ci.com/Johboh/hassalarm.svg?branch=master)](https://travis-ci.com/Johboh/hassalarm)
