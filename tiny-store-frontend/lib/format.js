FormatFactory = {
    get(formatName) {
        switch (formatName) {
            case "parameters": return ParametersFormat;
            case "simple-parameters": return SimpleParametersFormat;
            case "type": return TypeFormat;
            case "simple-type": return SimpleTypeFormat;
            case "version": return VersionFormat;
            case "time": return TimeFormat;
            case "class-name": return ClassNameFormat;
            default: throw `Not defined formatter class ${formatName}.`;
        }
    }
};

ParametersFormat = {
    format(parameters) {
        return `[ ${parameters.map(parameter => TypeFormat.format(parameter.type)).join(", ")} ]`;
    }
};

SimpleParametersFormat = {
    format(parameters) {
        return `[ ${parameters.map(parameter => SimpleTypeFormat.format(parameter.type)).join(", ")} ]`;
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

SimpleTypeFormat = {
    format(type) {
        function simpleName(qualifiedName) {
            return qualifiedName ? /[^.]+$/.exec(qualifiedName)[0] : null;
        }

        if (!type.collection) {
            return simpleName(type.name);
        }
        return `${simpleName(type.collection)}<${simpleName(type.name)}>`;
    }
};

VersionFormat = {
    format(version) {
        return `${version.major}.${version.minor}`;
    }
};

TimeFormat = {
    format(date) {
        if (typeof date == "string") {
            date = new Date(date);
        }
        function x(number) { return number < 10 ? `0${number}` : number; }
        return `${x(date.getHours())}:${x(date.getMinutes())}:${x(date.getSeconds())}`;
    }
};

ClassNameFormat = {
    format(name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }
};