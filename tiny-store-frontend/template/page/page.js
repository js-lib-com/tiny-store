/**
 * Pages base class.
 * 
 * @author Iulian Rotaru
 * @since 1.0
 * 
 * @constructor Construct an instance of a page class.
 */
Page = function() {
	this.$super();
};

Page.prototype = {
	onServerFail(er) {
		$error("Page#onServerFail", "%s: %s", er.cause, er.message);
		js.ua.System.error("Server error. Please contact administrator.");
	},

	/**
	 * Class string representation.
	 * 
	 * @return {string} this class string representation.
	 */
	toString : function() {
		return "Page";
	}
};
$extends(Page, js.ua.Page);
