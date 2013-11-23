package org.dumb.yaml.parser;

/**
 * Date: 11/20/13
 * Time: 12:56 AM
 *
 * @author Artem Prigoda
 */
class ParserNewStep {

    boolean ifContinue;
    int pos;

    public ParserNewStep(boolean ifContinue, int pos) {
        this.ifContinue = ifContinue;
        this.pos = pos;
    }
}
