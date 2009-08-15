/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.functions;

// @see PackageTest for unit tests

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Function to evaluate a string which may contain variable or function references.
 *
 * Parameter: string to be evaluated
 *
 * Returns: the evaluated value
 *
 */
public class EvalVarFunction extends AbstractFunction {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final List<String> desc = new LinkedList<String>();

    private static final String KEY = "__evalVar"; //$NON-NLS-1$

    // Number of parameters expected - used to reject invalid calls
    private static final int MIN_PARAMETER_COUNT = 1;
    private static final int MAX_PARAMETER_COUNT = 1;

    static {
        desc.add(JMeterUtils.getResString("evalvar_name_param")); //$NON-NLS-1$
    }

    private Object[] values;

    public EvalVarFunction() {
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
        String variableName = ((CompoundVariable) values[0]).execute();
        final JMeterVariables vars = getVariables();
        if (vars == null){
            log.error("Variables have not yet been defined");
            return "**ERROR - see log file**";
        }
        String variableValue = vars.get(variableName);
        CompoundVariable cv = new CompoundVariable(variableValue);
        return cv.execute();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
        values = parameters.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    public List<String> getArgumentDesc() {
        return desc;
    }

}