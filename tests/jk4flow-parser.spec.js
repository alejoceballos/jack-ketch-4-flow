// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

'use strict';

var JK4Flow = require('../src/jk4flow-parser');

describe('JSON Parsing', function() {

    it('Should not be able to parse an empty string', function() {
        var result = JK4Flow.parse('');

        expect(result.success).toBe(false);
    });

    it('Should be able to parse a complex JSON', function() {
        var A_JSON =
            '{' +
            '    "initial-node": { "id": "#1", "to-node": "#2" },' +
            '    "action-nodes": [' +
            '        { "id": "#2", "to-node": "#3", "callback": "TestAction::sum" },' +
            '        { "id": "#4", "to-node": "#6", "callback": "TestAction::displayRight" },' +
            '        { "id": "#5", "to-node": "#6", "callback": "TestAction::displayWrong" }' +
            '    ],' +
            '    "decision-nodes": [' +
            '        {' +
            '            "id": "#3",' +
            '            "to-nodes": [' +
            '                {' +
            '                    "to-node": "#4",' +
            '                    "context": {' +
            '                        "attribute": "result",' +
            '                        "value": "false"' +
            '                    }' +
            '                },' +
            '                {' +
            '                    "to-node": "#5",' +
            '                    "context": {' +
            '                        "attribute": "result",' +
            '                        "value": "true"' +
            '                    }' +
            '                }' +
            '            ]' +
            '        }' +
            '    ],' +
            '    "final-node": { "id": "#6" }' +
            '}';

        var result = JK4Flow.parse(A_JSON);

        expect(result.success).toBe(true);
    });

});
