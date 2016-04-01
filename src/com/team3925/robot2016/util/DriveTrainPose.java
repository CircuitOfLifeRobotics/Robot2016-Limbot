package com.team3925.robot2016.util;

public class DriveTrainPose {
    public DriveTrainPose(double leftDistance, double rightDistance, double leftVelocity,
                double rightVelocity, double heading, double headingVelocity) {
        this.m_left_distance = leftDistance;
        this.m_right_distance = rightDistance;
        this.m_left_velocity = leftVelocity;
        this.m_right_velocity = rightVelocity;
        this.m_heading = heading;
        this.m_heading_velocity = headingVelocity;
    }

    public double m_left_distance;
    public double m_right_distance;
    public double m_left_velocity;
    public double m_right_velocity;
    public double m_heading;
    public double m_heading_velocity;

    public void reset(double leftDistance, double rightDistance, double leftVelocity,
                      double rightVelocity, double heading, double headingVelocity) {
        this.m_left_distance = leftDistance;
        this.m_right_distance = rightDistance;
        this.m_left_velocity = leftVelocity;
        this.m_right_velocity = rightVelocity;
        this.m_heading = heading;
        this.m_heading_velocity = headingVelocity;
    }

    public double getLeftDistance() {
        return m_left_distance;
    }

    public double getHeading() {
        return m_heading;
    }

    public double getRightDistance() {
        return m_right_distance;
    }

    public double getLeftVelocity() {
        return m_left_velocity;
    }

    public double getRightVelocity() {
        return m_right_velocity;
    }

    public double getHeadingVelocity() {
        return m_heading_velocity;
    }

    public class RelativePoseGenerator {
        private DriveTrainPose m_base_pose;

        public RelativePoseGenerator() {
            m_base_pose = DriveTrainPose.this;
        }

        public DriveTrainPose get(DriveTrainPose pose) {
            return new DriveTrainPose(
                    pose.getLeftDistance() - m_base_pose.getLeftDistance(),
                    pose.getRightDistance() - m_base_pose.getRightDistance(),
                    m_base_pose.getLeftVelocity() - pose.getLeftVelocity(),
                    m_base_pose.getRightVelocity() - pose.getRightVelocity(),
                    pose.getHeading() - m_base_pose.getHeading(),
                    m_base_pose.getHeadingVelocity()
                            - pose.getHeadingVelocity());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DriveTrainPose))
            return false;
        if (obj == this)
            return true;
        DriveTrainPose other_pose = (DriveTrainPose) obj;
        return other_pose.getLeftDistance() == getLeftDistance()
                && other_pose.getRightDistance() == getRightDistance()
                && other_pose.getLeftVelocity() == getLeftVelocity()
                && other_pose.getRightVelocity() == getRightVelocity()
                && other_pose.getHeading() == getHeading()
                && other_pose.getHeadingVelocity() == getHeadingVelocity();
    }
}