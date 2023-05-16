$(document).ready(function() {
  // Handle hero form submission
  $("#hero-form").submit(function(event) {
    event.preventDefault();
    var form = $(this);
    var url = form.attr("action");
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

  // Handle squad form submission
  $("#squad-form").submit(function(event) {
    event.preventDefault();
    var form = $(this);
    var url = form.attr("action");
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

  // Handle assign hero form submission
  $(".assign-hero-form").submit(function(event) {
    event.preventDefault();
    var form = $(this);
    var url = form.attr("action");
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

  // Handle delete hero button click
  $(".delete-hero-btn").click(function(event) {
    event.preventDefault();
    var btn = $(this);
    var url = btn.attr("href");

    if (confirm("Are you sure you want to delete this hero?")) {
      $.ajax({
        type: "POST",
        url: url,
        success: function(response) {
          window.location.reload();
        }
      });
    }
  });

  // Handle delete squad button click
  $(".delete-squad-btn").click(function(event) {
    event.preventDefault();
    var btn = $(this);
    var url = btn.attr("href");

    if (confirm("Are you sure you want to delete this squad?")) {
      $.ajax({
        type: "POST",
        url: url,
        success: function(response) {
          window.location.reload();
        }
      });
    }
  });
});
