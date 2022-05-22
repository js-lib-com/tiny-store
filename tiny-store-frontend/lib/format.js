FormatFactory = {
    get(formatName) {
        switch (formatName) {
            case "parameters": return ParametersFormat;
            case "type": return TypeFormat;
            case "version": return VersionFormat;
            default: throw `Not defined formatter class ${formatName}.`;
        }
    }
};

ParametersFormat = {
    format(parameters) {
        return parameters.map(parameter => TypeFormat.format(parameter.type)).join(", ");
    }
}

TypeFormat = {
    format(type) {
        if (!type.collection) {
            return type.name;
        }
        return `${type.collection}<${type.name}>`;
    }
}

VersionFormat = {
    format(version) {
        return `${version.major}.${version.minor}`;
    }
}