OPP = {
    get: function (obj, opp) {
        if (obj == null) {
            return null;
        }
        return this._get(obj, opp.split("."), 0);
    },

    _get: function (obj, opp, i) {
        if (typeof obj !== "undefined" && obj !== null && i < opp.length) {
            obj = this._get(obj[opp[i++]], opp, i);
        }
        return obj;
    },

    set: function (obj, opp, value) {
        this._set(obj, opp.split("."), 0, value);
    },

    _set: function (obj, opp, i, value) {
        // iterate till OPP right most element
        if (i === opp.length - 1) {
            obj[opp[i]] = value;
            return;
        }

        if(typeof obj[opp[i]] === "undefined") {
            obj[opp[i]] = {};
        }
        obj = obj[opp[i]];

        if (obj === null || typeof obj !== "object") {
            return;
        }
        ++i;
        obj = this._set(obj, opp, i, value);
    }
};