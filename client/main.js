
function triggerCircuit() {
	var count = document.getElementById("count").value;
    var Rating = document.getElementById("Rating").value;
    var Type = document.getElementById("Type").value;
    var MailID = document.getElementById("mailid").value;
    document.getElementById("myForm").reset();

    if (count || Rating || Type || MailID) {
        console.log(count, Rating, Type, MailID);

        $.ajax({
            url: "/server/circuit/triggerCircuit",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify({
                "count": count,
                "rating": Rating,
                "traveller": Type,
                "mail_id": MailID
            }),
            success: function (data) {
                console.log(data); //You can view these logs from Logs in the Catalyst console
                document.getElementById("popuptext").innerHTML = data.message;
            },
            error: function (error) {
                console.log(error);
                document.getElementById("popuptext").innerHTML = "Please try again after sometime";
            }
        });
    } else {
        document.getElementById("popuptext").innerHTML = "Please provide data in the all the fields";
    }
}
