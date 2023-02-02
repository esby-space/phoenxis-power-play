package org.firstinspires.ftc.teamcode;

public interface RobotConfig {
    // drive configuration, values between 0 and 1
    double MECANUM_SPEED = 0.5;
    double ROTATION_SPEED = 0.6;
    double ARM_CONTROL_SPEED = 0.6;

    double WHEEL_DIAMETER = 3.779;
    double DRIVE_GEAR_REDUCTION = 1.0;
    double COUNTS_PER_REV = 537.7;
    double COUNTS_PER_INCH = (COUNTS_PER_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER * Math.PI);

    // arm configuration
    double ARM_SPEED = 0.5;
    double ARM_WHEEL_DIAMETER = 1.608;
    double ARM_COUNTS_PER_REV = 751.8;
    double ARM_COUNTS_PER_INCH = ARM_COUNTS_PER_REV / (ARM_WHEEL_DIAMETER * Math.PI);
}

// 14413!

// /\__/\
// (=o.o=)
// |/--\|
// (")-(")
