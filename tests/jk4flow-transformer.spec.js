// Set of JSHint directives
/* global describe: true */
/* global beforeEach: true */
/* global it: true */
/* global expect: true */

'use strict';

var JK4Flow = require('../src/jk4flow-transformer');

describe('JSON Transformation', function() {

    it('Should not be able to transform an empty string', function() {
        var result = JK4Flow.transform('');

        expect(result.success).toBe(false);
    });

    it('Should be able to transform a complex JSON', function() {
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
            '                    "id": "#4",' +
            '                    "context": {' +
            '                     "attribute": "result",' +
            '                     "value": "false"' +
            '                    }' +
            '                },' +
            '                {' +
            '                    "id": "#5",' +
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

        var result = JK4Flow.transform(A_JSON);

        expect(result.success).toBe(true);
    });

});
