$(document).ready(function() {
  // Handle assign hero form submission
  $(document).on("submit", ".assign-hero-form", function(event) {
    event.preventDefault();
    var form = $(this);
    var squadId = form.data("squad-id");
    var url = "/squads/" + squadId + "/heroes";
    var data = form.serialize();

    $.ajax({
      type: "POST",
      url: url,
      data: data,
      success: function(response) {
        window.location.reload();
      }
    });
  });
});
