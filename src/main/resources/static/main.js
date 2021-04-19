const authModeOn = isAuth => {
  document.getElementById("action").hidden = !isAuth;
  document.getElementById("login").hidden = isAuth;
  if (isAuth) {
    openCalendar();
  }
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

const multiselect = $('.multiselect').multipleSelect({
  selectAll: false,
  data: []
});

const getStatus = () => {
  actionForm = true
  ajax("status", {}, data => {
    if (data.username !== undefined) {
      document.querySelector("#username").innerText = data.username
    }
    clearTimeout(timer);
    timer = setTimeout(getStatus, 60000);
    ajax("api/members", {}, data => {
      multiselect.multipleSelect('refreshOptions', {
        selectAll: false,
        data: data.map(el => el.username)
      })
    });
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

function getEvent(start, end, timezone, callback) {
  console.log("getEvents")
  console.log(start.format(), end.format(), timezone);
  ajax("api/event", {start: start.format(), end: end.format()}, data => {
    console.log(data);
    callback(data);
  })
}

const openCalendar = () => {
  $('#calendar').fullCalendar({
    themeSystem: 'bootstrap4',
    defaultView: 'agendaWeek',
    firstDay: 1,
    timezone: 'UTC',
    timeFormat: 'H:mm',
    slotEventOverlap: false,
    displayEventTime: true,
    displayEventEnd: true,
    nextDayThreshold: "00:00:00",
    slotLabelInterval: '00:30',
    header: {
      left: 'title',
      right: 'today prev next'
    },
    allDaySlot: false,
    slotLabelFormat: 'HH:mm',
    events: getEvent,
    dayClick: function () {
      $('#modal-view-event-add').modal();
    },
    eventClick: function (event) {
      $('.event-creator').html(event.creator);
      $('.event-title').html(event.title);
      $('.event-time').html(
        `${moment(event.start).format('DD.MM.YYYY HH:mm')} - ${moment(event.end).format('DD.MM.YYYY HH:mm')}`);
      $('.event-body').html(event.description);
      const members = document.querySelector('.event-members')
      members.innerHTML = ''
      event.members.forEach(
        member => {
          const li = document.createElement('li');
          li.innerText = member.username
          members.appendChild(li)
        }
      );
      $('#modal-view-event').modal();
    }
  });
}

(() => {
    getStatus();
    document.querySelectorAll(".need-valid").forEach(node => {
      node.onchange = () => {
        changeTagValid(node, 'is-valid', 'is-invalid');
        checkButton();
      };
    });

    const timeFormat = 'hh:ii';
    const dateFormat = 'dd.mm.yyyy';
    const dateTimeFormat = 'DD.MM.YYYY HH:mm'

    const getTime = dateString => {
      console.log(dateString)
      const d = new Date(moment(dateString, dateTimeFormat).utc());
      console.log(d)
      return new Date(d.getTime() - (d.getTimezoneOffset() * 60000)).toISOString();
    }

    $('.datetimepicker').datepicker({
      timepicker: true,
      firstDay: "1",
      minutesStep: 30,
      language: 'en',

      time: true,
      timeFormat,
      dateFormat
    });

    $("#add-event").submit(() => {
      event.preventDefault();
      var values = {};
      $.each($('#add-event').serializeArray(), function (i, field) {
        console.log(field.name)
        values[field.name] = field.value;
      });
      console.log(values)
      ajax("api/create", {
        title: values.ename,
        description: values.edesc,
        start: getTime(values.edatestart),
        end: getTime(values.edateend),
        members: values.members.split(',').map(el => {
          return {
            username: el
          }
        })
      }, data => {
        console.log(data);
        $('#calendar').fullCalendar('refetchEvents');
      })
    });
  }
)();
