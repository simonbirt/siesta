/*
 * Copyright (c) 2017 Cadenza United Kingdom Limited
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.cadenzauk.siesta.grammar.expression;

import com.cadenzauk.core.sql.RowMapper;
import com.cadenzauk.siesta.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConcatOperator<T, U>  implements TypedExpression<String> {
    private final List<TypedExpression<?>> operands = new ArrayList<>();

    public ConcatOperator(TypedExpression<T> lhs, TypedExpression<U> rhs) {
        operands.add(lhs);
        operands.add(rhs);
    }

    @Override
    public String sql(Scope scope) {
        return scope.dialect().concat(operands.stream().map(op -> sql(op, scope)));
    }

    @Override
    public Stream<Object> args(Scope scope) {
        return operands.stream().flatMap(op -> op.args(scope));
    }

    @Override
    public Precedence precedence() {
        return Precedence.CONCAT;
    }

    @Override
    public String label(Scope scope) {
        return operands.get(0).label(scope);
    }

    @Override
    public RowMapper<String> rowMapper(Scope scope, String label) {
        return Scope.makeMapper(String.class).apply(scope, label);
    }
}
