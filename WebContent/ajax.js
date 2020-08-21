const http = window.XMLHttpRequest ? new XMLHttpRequest()
		: ActiveXObject("Microsoft.XMLHTTP");
const ajaxType = {
	GET : "get",
	POST : "post"
};

/**
 * 将json格式数据转为字符串
 * 
 * @param {*}
 *            data
 */
function json2String(data) {
	let res = "";
	let key;
	for (key in data) {
		res = res + "&" + key + "=" + data[key];
	}
	res = res.substr(1);
	return res;
}

function ajax(ajaxJson) {
	async = ajaxJson.async == null ? true : ajaxJson.async;
	if (ajaxJson.type == ajaxType.GET) {
		http.open("GET", ajaxJson.url, async);
		http.send();
	} else if (ajaxJson.type == ajaxType.POST) {
		http.open("POST", ajaxJson.url, async);
		http.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		http.send(json2String(ajaxJson.data));
	}
	http.onreadystatechange = function() {
		if (http.readyState == 4 && http.status == 200) {
			if (ajaxJson.success != null) {
				ajaxJson.success(http.responseText);
			}
		} else if (http.readyState == 4 || http.status != 200) {
			if (ajaxJson.error != null) {
				ajaxJson.error(http.responseText);
			}
		}
	}
}
function get(ajaxJson) {
	async = ajaxJson.async == null ? true : ajaxJson.async;
	http.open("POST", ajaxJson.url, async);
	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.send(json2String(ajaxJson.data));
	http.onreadystatechange = function() {
		if (http.readyState == 4 && http.status == 200) {
			if (ajaxJson.success != null) {
				ajaxJson.success(http.responseText);
			}
		} else if (http.readyState == 4 || http.status != 200) {
			if (ajaxJson.error != null) {
				ajaxJson.error(http.responseText);
			}
		}
	}
}
function post(ajaxJson) {
	async = ajaxJson.async == null ? true : ajaxJson.async;
	http.open("GET", ajaxJson.url, async);
	http.send();
	http.onreadystatechange = function() {
		if (http.readyState == 4 && http.status == 200) {
			if (ajaxJson.success != null) {
				ajaxJson.success(http.responseText);
			}
		} else if (http.readyState == 4 || http.status != 200) {
			if (ajaxJson.error != null) {
				ajaxJson.error(http.responseText);
			}
		}
	}
}
