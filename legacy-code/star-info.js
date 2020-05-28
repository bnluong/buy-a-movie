/**
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */

/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
	// Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Use regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"), results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleResult(resultData) {
    console.log("handleResult: populating star info from resultData");

    // Populate the page title <id="title_name"> with the star's name
    let titleHTML = jQuery("#title_name");
    titleHTML.append(resultData[0]["star_name"]);
    
    // Populate <id="personal_info_table_body"> with the star's id and birth year
    let personalInfoHTML = jQuery("#personal_info_table_body");
    let personalInfoRowHTML = "";
    personalInfoRowHTML += "<tr>";
    personalInfoRowHTML += "<td><strong>" + "ID" + "</strong></td>";
    personalInfoRowHTML += "<td>" + resultData[0]["star_id"] + "</td>";
    personalInfoRowHTML += "</tr>";
    personalInfoRowHTML += "<tr>";
    personalInfoRowHTML += "<td><strong>" + "Birth Year" + "</strong></td>";
    if(resultData[0]["star_birth_year"] != null)
    	personalInfoRowHTML += "<td>" + resultData[0]["star_birth_year"] + "</td>";
    else
    	personalInfoRowHTML += "<td>" + "No Information" + "</td>";
    personalInfoRowHTML += "</tr>";
    personalInfoHTML.append(personalInfoRowHTML);
    
    // Populate the <id="star_name"> with the star's name
    let starNameHTML = jQuery("#star_name");
    starNameHTML.append("<strong>" + resultData[0]["star_name"] + "</strong>");
    
    // Populate the <id=filmography_table_body> with the star's filmography
    let filmographyTableBodyHTML = jQuery("#filmography_table_body");
    let filmographyRowHTML = "";
    
    var filmography = resultData[0]["star_filmography"];
    for(let i = 0; i < filmography.length; i++) {
        filmographyRowHTML += "<tr>";
        filmographyRowHTML += "<td>" + filmography[i]["movie_year"] + "</td>";
        filmographyRowHTML += "<td>"; 
        filmographyRowHTML += '<a href="movie-info.html?id=' + filmography[i]["movie_id"] + '">' + filmography[i]["movie_title"] + "</a>";
        filmographyRowHTML += "</td>";
        filmographyRowHTML += "</tr>";	
    }
    
    filmographyTableBodyHTML.append(filmographyRowHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */
// Get id from URL
let starId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  						// Setting return data type
    method: "GET",							// Setting request method
    url: "api/star-info?id=" + starId, 		// Setting request url, which is mapped by the servlet
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the the servlet
});