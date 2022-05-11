FormatFactory = {
    get(className) {
        switch (className) {
            case "ParametersFormat": return ParametersFormat;
            case "TypeFormat": return TypeFormat;
            case "BooleanFormat": return BooleanFormat;
            default: throw `Not defined formatter class ${className}.`;
        }
    }
};

ParametersFormat = {
    format(parameters) {
        return "";
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

BooleanFormat = {
    format(object) {

    },

    parse(string) {

    }
}