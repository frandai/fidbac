//GLOBAL VARIABLES
var _answersData, _startTime, _usersAnswered = 0, _sectionsTime = [0], _maxTime,
        _myCharts = [], _feelingsConfiguration = [];

//KEEP SESSION ALIVE
function keepSessionAlive() {
    $.post("ping.html");
}

$(function () {
    window.setInterval("keepSessionAlive()", 60000);
});
//END KEEP SESSION ALIVE 


function confirmStartPresentation() {
    return confirm('You are going to start the presentation with the configured data. Are you sure?')
}

function goToPresentation(code) {
    if (code !== '') {
        location.href = (location.href.indexOf("view/") > -1 ? '' : './view/') + 'client.xhtml?code=' + encodeURIComponent(code);
    }
}

function generateQR(code) {
    var url = '' + (window.location);
    var index = url.lastIndexOf("/");
    if (index !== -1) {
        url = url.substring(0, index);
    }
    window.open('http://es.qr-code-generator.com/phpqrcode/getCode.php?cht=qr&chl=' + encodeURIComponent(
            url + '/client.xhtml?code=' + encodeURIComponent(code)),
            "_blank", "toolbar=no, scrollbars=no, width=400, height=400");
    return false;
}

function showAnswers(options) {
    PF('questions').hide();
    setTimeout(function () {
        PF('answers').show();
        loadAnswersChart(options);
    }, 500);
}

//SOCKETS
function handleSection(section) {
    if (section.title === '...EndOfPresentaton...') {
        alert('End of presentation. Thank you.');
        location.href = (location.href.indexOf("view/") > -1 ? '' : './view/') + 'index.xhtml';
        return;
    }
    var sectDoc = '<strong>' + section.title + '</strong><br/>' + section.description;
    document.getElementById('form_section:new_section_description').innerHTML = sectDoc;
    PF('newSection').show();
    PF('sectionDef').header.children('h3').html(section.title);
    document.getElementById('form_body:body_page:section_description').textContent = section.description;
}

function handleQuestion(question) {
    if (question.open) {
        PF('question' + question.id).show();
    } else {
        alert('The question will be closed in 10 seconds.');
        setTimeout(function () {
            PF('question' + question.id).hide();
        }, 10000);
    }
}

function handleAnswer(answer) {
    if (answer.text) {
        alert('Somebody ask: ' + answer.text);
    } else {
        if (answer.correct) {
            _answersData.correct++;
        } else {
            _answersData.incorrect++;
        }
        for (var i = 0; i < _answersData.options.length; ++i) {
            for (var j = 0; j < answer.option.length; ++j) {
                if (_answersData.options[i] === answer.option[j]) {
                    _answersData.series[i] = (_answersData.series[i] + 1);
                }
            }
        }
        _usersAnswered++;
        refreshAnswersChart();
    }
}

function handleFeeling(feeling) {
    for (var i = 0; i < _myCharts.length; i++) {
        if (_myCharts[i].chart === feeling.filter) {
            _myCharts[i].handleAFeeling(feeling);
        }
    }
}

function handleNewSection(final) {
    _sectionsTime[_sectionsTime.length] = getCurrentMinutesForPresentation();
    refreshAllCharts();
    if (final) {
        _maxTime = new Date();
    }
}
//END SOCKETS


function refreshAllCharts(live) {
    for (var i = 0; i < _myCharts.length; i++) {
        _myCharts[i].refreshFeelingsChart(live);
    }
}

function getCurrentMinutesForPresentation(newCurrent) {
    var current = newCurrent ? newCurrent : new Date();
    return ((current - _startTime) / (1000.0 * 60.0));
}

// ANSWER CHART
function loadAnswersChart(options) {
    var series = [];
    _usersAnswered = 0;
    for (var i = 0; i < options.length; ++i) {
        series[i] = 0.1;
    }
    _answersData = {
        options: options,
        series: series,
        correct: 0,
        incorrect: 0
    };
    refreshAnswersChart();
}

function refreshAnswersChart() {
    var labels = [];
    for (var i = 0; i < _answersData.options.length; ++i) {
        labels[i] = _answersData.options[i] + ' (' + Math.floor(_answersData.series[i]) + ')';
    }
    var optionsData = {
        labels: labels,
        series: _answersData.series
    };

    var ciData = {
        labels: ['Correct (' + Math.floor(_answersData.correct) + ')', 'Incorrect (' + Math.floor(_answersData.incorrect) + ')'],
        series: [_answersData.correct + 0.1, _answersData.incorrect + 0.1]
    };
    loadAnswersChartWithData(optionsData, 'answers-options-chart');
    loadAnswersChartWithData(ciData, 'answers-ci-chart');

    var user_answer_label = document.getElementsByClassName('users_answered');
    for (var i = 0; i < user_answer_label.length; ++i) {
        var item = user_answer_label[i];
        item.innerHTML = _usersAnswered + ' of ' + _myCharts[0]._numUsersConnected + ' users have answered.';
    }
}

function loadAnswersChartWithData(data, chart) {
    var chartOptions = {
        distributeSeries: true,
        height: 150,
        width: '100%',
        axisY: {
            onlyInteger: true,
            showGrid: true
        }
    };
    new Chartist.Bar('.' + chart, data, chartOptions);
}
//END ANSWER CHART

// FEELING CHART

function buildSectionSerie(maxNum) {
    var sectionData = [];

    for (var i = 0; i < _sectionsTime.length; ++i) {
        sectionData[sectionData.length] = {x: _sectionsTime[i] - (1 / 600), y: 0};
        sectionData[sectionData.length] = {x: _sectionsTime[i], y: maxNum};
        sectionData[sectionData.length] = {x: _sectionsTime[i] + (1 / 600), y: 0};
    }

    return {
        className: 'changeSection',
        data: sectionData,
        lineSmooth: Chartist.Interpolation.step({
            postpone: false
        }),
        name: 'changeSectionNotFeeling'
    };
}

// END FEELING CHART

function addChart(newChart) {
    var generatedChart = generateChart(newChart);
    generatedChart.refreshFeelingsChart();
    _myCharts[_myCharts.length] = generatedChart;
}

function generateChart(achart) {
    var theChart = {
        chart: achart,
        _feelingsSeries: [],
        _happinessSeries: [{x: 0, y: 0}],
        _numUsersConnected: 0,
        _numUsersLoggedConnected: 0,
        _maxNumUsersConnected: 0,
        _maxNumUsersLoggedConnected: 0,
        _usersConnected: [],
        _maxAmount: 1,
        _maxHappiness: 1,
        _dataLoaded: false,
        _average: false,
        dataToTime: function (time) {
            for (var i = 0; i < this._feelingsSeries.length; ++i) {
                var feldata = this._feelingsSeries[i].data;
                if (feldata[feldata.length - 1].x < time) {
                    if (feldata[feldata.length - 1].virtual) {
                        feldata[feldata.length - 1] = {x: time, y: feldata[feldata.length - 1].y, virtual: true};
                    } else {
                        feldata[feldata.length] = {x: time, y: feldata[feldata.length - 1].y, virtual: true};
                    }
                }
            }
            if (this._happinessSeries[this._happinessSeries.length - 1].x < time) {
                if (this._happinessSeries[this._happinessSeries.length - 1].virtual) {
                    this._happinessSeries[this._happinessSeries.length - 1] = {x: time,
                        y: this._happinessSeries[this._happinessSeries.length - 1].y, virtual: true};
                } else {
                    this._happinessSeries[this._happinessSeries.length] = {x: time,
                        y: this._happinessSeries[this._happinessSeries.length - 1].y, virtual: true};
                }
            }
        },
        getMed: function (fdata) {
            if(!this._average) {
                return fdata[fdata.length - 1].y;
            } else {
                var data = 0;
                for(var j = 0; j < fdata.length; j++) {
                    data += fdata[j].y;
                }
                return (data / fdata.length);
            }
        },
        refreshFeelingsChart: function (live) {

            this.dataToTime(live ? getCurrentMinutesForPresentation() : _sectionsTime[_sectionsTime.length - 1]);

            var feelSeries = this._feelingsSeries.concat(buildSectionSerie(this._maxAmount)),
                    happySeries = [this._happinessSeries].concat(buildSectionSerie(this._maxHappiness)),
                    axisX = {
                        type: Chartist.AutoScaleAxis,
                        onlyInteger: true,
                        low: 0
                    };

            if (_maxTime) {
                axisX.high = _maxTime;
            }

            new Chartist.Line('.feeling-' + this.chart + '-chart', {
                series: feelSeries
            }, {
                axisX: axisX,
                axisY: {
                    type: Chartist.AutoScaleAxis,
                    onlyInteger: true
                },
                height: 300,
                low: 0,
                lineSmooth: Chartist.Interpolation.simple(),
                showArea: true,
                showPoint: true,
                fullWidth: true,
                referenceValue: 0,
                stretch: true
            });

            new Chartist.Line('.happiness-' + this.chart + '-chart', {
                series: happySeries
            }, {
                axisX: axisX,
                axisY: {
                    type: Chartist.AutoScaleAxis,
                    onlyInteger: true
                },
                height: 300,
                lineSmooth: Chartist.Interpolation.simple(),
                showArea: true,
                showPoint: true,
                fullWidth: true,
                low: -100,
                referenceValue: 0,
                stretch: true
            });

            var barLabels = [], barSeries = [], fdata, icon = '13', maxS = 0;

            for (var i = 0; i < this._feelingsSeries.length; ++i) {
                barLabels[i] = this._feelingsSeries[i].name;
                fdata = this._feelingsSeries[i].data;
                barSeries[i] = this.getMed(fdata);
                if (maxS < barSeries[i]) {
                    maxS = barSeries[i];
                    icon = this._feelingsSeries[i].icon;
                }
            }

            new Chartist.Bar('.feeling-bar-' + this.chart + '-chart', {
                labels: barLabels,
                series: barSeries
            }, {
                distributeSeries: true,
                height: 300,
                width: '100%',
                axisY: {
                    onlyInteger: !this._average,
                    showGrid: true
                }
            });

            var icons_feeling = document.getElementsByClassName('' + this.chart + '_icon_feeling');
            for (var i = 0; i < icons_feeling.length; ++i) {
                var item = icons_feeling[i];
                item.className = this.chart + '_icon_feeling big_icon_feeling bfeeling_' + icon;
            }

            var user_label = document.getElementsByClassName('' + this.chart + '_users_connected');
            for (var i = 0; i < user_label.length; ++i) {
                var item = user_label[i];
                item.innerHTML = 'Users online: ' + this._numUsersConnected + ' (' + this._numUsersLoggedConnected + ' logged)<br/>'
                        + 'Max. Users online: ' + this._maxNumUsersConnected + ' (' + this._maxNumUsersLoggedConnected + ' logged): ';
            }
        },
        fillUsers: function (feeling) {
            var me = this;
            me._numUsersConnected = 0;
            me._numUsersLoggedConnected = 0;
            if (feeling.currentFeeling === -1) {
                me._usersConnected[('' + feeling.userId)] = 0;
            } else {
                if (!me._usersConnected[('' + feeling.userId)]) {
                    me._usersConnected[('' + feeling.userId)] = 1;
                }
            }

            Object.keys(me._usersConnected).forEach(function (i) {
                if (Number(i) > 0) {
                    me._numUsersConnected += me._usersConnected[i];
                    me._numUsersLoggedConnected += me._usersConnected[i];
                } else {
                    me._numUsersConnected += me._usersConnected[i];
                }
                me._maxNumUsersConnected = (me._maxNumUsersConnected < me._numUsersConnected) ? me._numUsersConnected : me._maxNumUsersConnected;
                me._maxNumUsersLoggedConnected =
                        (me._maxNumUsersLoggedConnected < me._numUsersLoggedConnected) ? me._numUsersLoggedConnected : me._maxNumUsersLoggedConnected;
            });
        },
        handleAFeeling: function (feeling) {
            var feelingData, amount,
                    happiness = this._happinessSeries[this._happinessSeries.length - 1].y;

            this.fillUsers(feeling);

            for (var i = 0; i < this._feelingsSeries.length; ++i) {
                feelingData = this._feelingsSeries[i].data;
                amount = feelingData[feelingData.length - 1].y;

                if (feeling.currentFeeling === this._feelingsSeries[i].feelingId) {
                    amount++;
                    happiness += ((this._feelingsSeries.length) - i);
                }
                if (feeling.previousFeeling === this._feelingsSeries[i].feelingId && amount > 0) {
                    amount--;
                    happiness -= ((this._feelingsSeries.length) - i);
                }

                var previousVirtual = feelingData[feelingData.length - 1].virtual;

                feelingData[previousVirtual ? feelingData.length - 1 : feelingData.length] =
                        {x: feeling.time ? feeling.time : getCurrentMinutesForPresentation(), y: amount};
                this._maxAmount = this._maxAmount < amount ? amount : this._maxAmount;
            }
            var previousVirtual = this._happinessSeries[this._happinessSeries.length - 1].virtual;
            this._happinessSeries[previousVirtual ? this._happinessSeries.length - 1 : this._happinessSeries.length] =
                    {x: feeling.time ? feeling.time : getCurrentMinutesForPresentation(), y: happiness};

            this._maxHappiness = this._maxHappiness < happiness ? happiness : this._maxHappiness;

            this.refreshFeelingsChart();

            if (!this._dataLoaded) {
                var load_div = document.getElementsByClassName('loadData_' + this.chart);
                for (var i = 0; i < load_div.length; ++i) {
                    var item = load_div[i];
                    item.style.display = 'none';
                }
                this._dataLoaded = true;
            }
        },
        hydrateFeelings: function () {
            for (var i = 0; i < _feelingsConfiguration.length; ++i) {
                this._feelingsSeries[i] = {
                    name: _feelingsConfiguration[i].name,
                    feelingId: _feelingsConfiguration[i].feelingId,
                    icon: _feelingsConfiguration[i].icon,
                    data: [{x: 0, y: 0}],
                    className: ''
                };
            }
        },
        hydrateFeelingsData: function (data) {
            for (var i = 0; i < data.length; i++) {
                this.handleAFeeling(data[i]);
            }
        }
    };
    theChart.hydrateFeelings();

    return theChart;
}

function hydrateChartWithData(chart, data) {
    for (var i = 0; i < _myCharts.length; i++) {
        if (_myCharts[i].chart === chart && data.length > 0) {
            _myCharts[i].hydrateFeelingsData(data);
        }
    }
}

function hydeShowChart(chart, feelingId) {
    for (var i = 0; i < _myCharts.length; i++) {
        if (_myCharts[i].chart === chart) {
            var fSerie = _myCharts[i]._feelingsSeries;
            for (var j = 0; j < fSerie.length; j++) {
                if (fSerie[j].feelingId === feelingId) {
                    fSerie[j].className = ((fSerie[j].className === 'hiddenSection') ? '' : 'hiddenSection');
                }
            }
            _myCharts[i].refreshFeelingsChart();
        }
    }
}

function switchAverageData(chart) {
    for (var i = 0; i < _myCharts.length; i++) {
        if (_myCharts[i].chart === chart) {
            _myCharts[i]._average = (!_myCharts[i]._average);
            _myCharts[i].refreshFeelingsChart();
        }
    }
}