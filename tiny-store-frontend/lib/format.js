FormatFactory = {
    get(formatName) {
        switch (formatName) {
            case "parameters": return ParametersFormat;
            case "type": return TypeFormat;
            case "version": return VersionFormat;
            case "time": return TimeFormat;
            default: throw `Not defined formatter class ${formatName}.`;
        }
    }
};

ParametersFormat = {
    format(parameters) {
        return parameters.map(parameter => TypeFormat.format(parameter.type)).join(", ");
    }
};

TypeFormat = {
    format(type) {
        if (!type.collection) {
            return type.name;
        }
        return `${type.collection}<${type.name}>`;
    }
};

VersionFormat = {
    format(version) {
        return `${version.major}.${version.minor}`;
    }
}

TimeFormat = {
    format(date) {
        if (typeof date == "string") {
            date = new Date(date);
        }
        function x(number) { return number < 10 ? `0${number}` : number; }
        return `${x(date.getHours())}:${x(date.getMinutes())}:${x(date.getSeconds())}`;
    }
};