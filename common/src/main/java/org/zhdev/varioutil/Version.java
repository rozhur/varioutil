package org.zhdev.varioutil;

import org.jetbrains.annotations.NotNull;

public final class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int patch;
    private final State state;
    private final String string;

    public Version(int major, int minor, int patch, @NotNull State state) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.state = state;
        this.string = major + "." + minor + (patch > 0 ? "." + patch : "") + (state != State.RELEASE ? '-' + state.name() : "");
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public State getState() {
        return state;
    }

    public int compareTo(int major, int minor, int patch, State state) {
        int result = this.major - major;
        if (result == 0) {
            result = this.minor - minor;
            if (result == 0) {
                result = this.patch - patch;
                if (result == 0) {
                    result = this.state.compareTo(state);
                }
            }
        }
        return result;
    }

    @Override
    public int compareTo(Version version) {
        return compareTo(version.major, version.minor, version.patch, version.state);
    }

    public int compareTo(int major, int minor, int patch) {
        int result = this.major - major;
        if (result == 0) {
            result = this.minor - minor;
            if (result == 0) {
                result = this.patch - patch;
            }
        }
        return result;
    }

    public int compareTo(int major, int minor) {
        int result = this.major - major;
        if (result == 0) {
            result = this.minor - minor;
        }
        return result;
    }

    public boolean equals(int major, int minor, int patch, State state) {
        return major == this.major && minor == this.minor && patch == this.patch && this.state == state;
    }

    public boolean equals(int major, int minor, int patch) {
        return major == this.major && minor == this.minor && patch == this.patch;
    }

    public boolean equals(int major, int minor) {
        return major == this.major && minor == this.minor;
    }

    public boolean lessThan(Version version) {
        return compareTo(version) < 0;
    }

    public boolean lessThan(int major, int minor, int patch, State state) {
        return compareTo(major, minor, patch, state) < 0;
    }

    public boolean lessThan(int major, int minor, int patch) {
        return compareTo(major, minor, patch) < 0;
    }

    public boolean lessThan(int major, int minor) {
        return compareTo(major, minor) < 0;
    }

    public boolean moreThan(Version version) {
        return compareTo(version) > 0;
    }

    public boolean moreThan(int major, int minor, int patch, State state) {
        return compareTo(major, minor, patch, state) > 0;
    }

    public boolean moreThan(int major, int minor, int patch) {
        return compareTo(major, minor, patch) > 0;
    }

    public boolean moreThan(int major, int minor) {
        return compareTo(major, minor) > 0;
    }

    public boolean moreOrEqual(Version version) {
        return compareTo(version) >= 0;
    }

    public boolean moreOrEqual(int major, int minor, int patch, State state) {
        return compareTo(major, minor, patch, state) >= 0;
    }

    public boolean moreOrEqual(int major, int minor, int patch) {
        return compareTo(major, minor, patch) >= 0;
    }

    public boolean moreOrEqual(int major, int minor) {
        return compareTo(major, minor) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (major != version.major) return false;
        if (minor != version.minor) return false;
        if (patch != version.patch) return false;
        return state == version.state;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return string;
    }

    public static Version fromString(String string) {
        String[] semver = string.split("\\.", 3);
        if (semver.length < 2) {
            throw new IllegalArgumentException("Incorrect semver (" + string + ")");
        }

        int major = Integer.parseInt(semver[0]);
        int minor;

        String[] split;
        int patch;
        if (semver.length < 3) {
            split = semver[1].split("-|\\s", 2);
            minor = Integer.parseInt(split[0]);
            patch = 0;
        } else {
            split = semver[2].split("-|\\s", 2);
            minor = Integer.parseInt(semver[1]);
            patch = Integer.parseInt(split[0]);
        }

        State state;
        if (split.length > 1) {
            state = State.valueOf(split[1].toUpperCase());
        } else {
            state = State.RELEASE;
        }

        return new Version(major, minor, patch, state);
    }

    public enum State {
        SNAPSHOT,
        ALPHA,
        BETA,
        RELEASE
    }
}
