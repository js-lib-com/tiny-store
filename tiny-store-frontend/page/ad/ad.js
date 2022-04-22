/**
 * Add description for ad page class.
 * 
 * @author ${author}
 * @since 1.0
 * 
 * @constructor Construct an instance of ad page class.
 */
ad = function() {
	this.$super();
};

ad.prototype = {
	/**
	 * Class string representation.
	 * 
	 * @return {string} this class string representation.
	 */
	toString : function() {
		return "ad";
	}
};
$extends(ad, Page);
