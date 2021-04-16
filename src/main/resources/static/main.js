const authModeOn = isAuth => {
  document.getElementById("action").hidden = !isAuth;
  document.getElementById("login").hidden = isAuth;
}

const toggleText = element => {
  const text = element.getAttribute("_toggle");
  if (text !== undefined) {
    element.setAttribute("_toggle", element.innerText);
    element.innerText = text;
  }
}

let actionForm = false;

let registeredForm = false;

const registration = () => {
  registeredForm = !registeredForm;
  document.getElementById("loginButton").hidden = registeredForm;
  toggleText(document.getElementById("toggleForm"));
  document.getElementById("registrationButton").hidden = !registeredForm;
  document.getElementById("status_registration").innerText = "";
}

const ajax = (url, data, callback) => {
  fetch(`/${url}`, {
    method: 'post',
    headers: {
      "Content-type": "application/json; charset=UTF-8;"
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      const auth = response.status !== 403
      actionForm &= auth
      authModeOn(actionForm)
      if (auth && response.status !== 404) {
        response.json().then(json => {
          if (callback !== undefined) {
            callback(json);
          }
        })
      }
    })
}

let timer;

const getStatus = () => {
  actionForm = true
  ajax("status", {}, () => {
    clearTimeout(timer);
    timer = setTimeout(getStatus, 5000);
  })
}

const act = (action, fun) => {
  ajax(action, {}, fun);
}

const userFormAjax = (action, fun) => {
  const form = document.getElementById("loginForm");
  const user = {login: form.elements.login.value, password: form.elements.password.value};
  ajax(action, user, fun);
}

const logIn = () => {
  userFormAjax("log-in", function (status) {
    document.getElementById("status_registration").innerText = !status.success ? status.message : "";
    actionForm = status.success;
    getStatus();
  });
}

const logOut = () => {
  act('log-out', () => {
    actionForm = false;
    getStatus();
  });
}

const registrationAccount = () => {
  userFormAjax("registration", status => {
    document.getElementById("status_registration").innerText = status.message;
  });
}

const validate = {};

const changeTagValid = (el, tagTrue, tagFalse) => {
  const flag = el.validity.valid;
  validate[el.name] = flag;
  el.classList.remove(flag ? tagFalse : tagTrue);
  el.classList.add(flag ? tagTrue : tagFalse);
}

const checkValid = (...tags) => {
  let flag = true;
  tags.forEach(el => flag &= validate[el])
  return flag;
}

const checkButton = () => {
  const flag = checkValid("login", "password");
  document.getElementById("loginButton").disabled = !flag;
  document.getElementById("registrationButton").disabled = !flag;
}

const events = [
  {
    title: 'Event',
    description: 'description event',
    start: '2021-04-15T13:00:00',
    end: '2021-04-15T14:00:00',
    className: 'fc-bg-pinkred',
    icon: "group",
    allDay: false
  },
  {
    title: 'Flight Paris',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu pellentesque nibh. In nisl nulla, convallis ac nulla eget, pellentesque pellentesque magna.',
    start: '2021-04-08T14:00:00',
    end: '2021-04-08T20:00:00',
    className: 'fc-bg-deepskyblue',
    icon: "cog",
    allDay: false
  },
  {
    title: 'Team Meeting',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu pellentesque nibh. In nisl nulla, convallis ac nulla eget, pellentesque pellentesque magna.',
    start: '2021-04-10T13:00:00',
    end: '2021-04-10T16:00:00',
    className: 'fc-bg-pinkred',
    icon: "group",
    allDay: false
  },
  {
    title: 'Restaurant',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu pellentesque nibh. In nisl nulla, convallis ac nulla eget, pellentesque pellentesque magna.',
    start: '2021-04-15T09:30:00',
    end: '2021-04-15T11:45:00',
    className: 'fc-bg-default',
    icon: "glass",
    allDay: false
  },
  {
    title: 'Dinner',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu pellentesque nibh. In nisl nulla, convallis ac nulla eget, pellentesque pellentesque magna.',
    start: '2021-04-15T20:00:00',
    end: '2021-04-15T22:30:00',
    className: 'fc-bg-default',
    icon: "cutlery",
    allDay: false
  },
  {
    title: 'Dentist',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu pellentesque nibh. In nisl nulla, convallis ac nulla eget, pellentesque pellentesque magna.',
    start: '2021-04-29T11:30:00',
    end: '2021-04-29T012:30:00',
    className: 'fc-bg-blue',
    icon: "medkit",
    allDay: false
  }];

function getEvent(start, end, timezone, callback) {
  console.log("getEvents")
  console.log(start.format(), end.format(), timezone);
  callback(events)
}

$(document).ready(() => {
  $('.datetimepicker').datepicker({
    timepicker: true,
    language: 'en',
    range: true,
    multipleDates: true,
    multipleDatesSeparator: " - "
  });

  $("#add-event").submit(() => {
    event.preventDefault();
    var values = {};
    $.each($('#add-event').serializeArray(), function (i, field) {
      values[field.name] = field.value;
    });
    console.log("create event:");
    console.log(values);
    $('#calendar').fullCalendar('refetchEvents');
  });
});

(() => {
    getStatus();
    document.querySelectorAll(".need-valid").forEach(node => {
      node.onchange = () => {
        changeTagValid(node, 'is-valid', 'is-invalid');
        checkButton();
      };
    });

    $(() => {
      setTimeout(() => jQuery('#calendar').fullCalendar({
        themeSystem: 'bootstrap4',
        defaultView: 'agendaWeek',
        header: {
          left: 'title',
          right: 'today prev next'
        },
        allDaySlot: false,
        slotLabelFormat: 'HH:mm',
        minTime: "06:00:00",
        maxTime: "20:00:00",
        events: getEvent,
        eventRender: function (event, element) {
          if (event.icon) {
            element.find(".fc-title").prepend("<i class='fa fa-" + event.icon + "'></i>");
          }
        },
        dayClick: function () {
          $('#modal-view-event-add').modal();
        },
        eventClick: function (event, jsEvent, view) {
          $('.event-icon').html("<i class='fa fa-" + event.icon + "'></i>");
          $('.event-title').html(event.title);
          $('.event-body').html(event.description);
          $('.eventUrl').attr('href', event.url);
          $('#modal-view-event').modal();
        }
      }), 150);
    });
  }
)();
