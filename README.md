# trinity phoenixes 2022 - 2023

trinity school robotics team code repository

## intro

hello software team! here is the code for the 2022 - 2023 ftc robotics season! this should be updated whenever there are updates to the code. would highly recommend **github desktop** for this - you can link it to android studio code!

## file descriptions

there are many files in this repo. here are the only ones that are important for us:

- `FTC.md` - information given by FTC, shows how to run sample code
- `README.md` - what you're reading now!
- `Robot.java` - methods to init and control the robot
- `RobotConfig.java` - defines important constants for the robot
- `teleop/TeleOPComp.java` - code running during driver control period
- `auton/AprilAuton.java` - code running during auton period
- `apriltag/AprilTagAutonomousInitDetectionExample.java` - run before auton to make sure camera is positioned correctly

## robot configuration

| name       | device        | port      |
|------------|---------------|-----------|
| frontLeft  | control hub   | 0         |
| frontRight | control hub   | 1         |
| backLeft   | control hub   | 2         |
| backRight  | control hub   | 3         |
| armExtend  | expansion hub | 0         |
| claw       | expansion hub | (servo) 0 |
| webcam     | Webcam 1      | -         |

## teleop mapping

| action       | device    | mapping             |
|--------------|-----------|---------------------|
| forward      | gamepad 1 | left stick up       |
| backward     | gamepad 1 | left stick down     |
| strafe left  | gamepad 1 | left stick left     |
| strafe right | gamepad 1 | left stick right    |
| turn left    | gamepad 1 | right stick left    |
| turn right   | gamepad 1 | right stick right   |
| fine tune    | gamepad 1 | triggers            |
| arm ground   | gamepad 2 | a button            |
| arm low      | gamepad 2 | x button            |
| arm medium   | gamepad 2 | y button            |
| arm high     | gamepad 2 | b button            |
| toggle claw  | gamepad 2 | left bumber         |

reach out to lake or seabass for anything!

```
14413!

/\__/\
(=o.o=)
|/--\|
(")-(")
```
