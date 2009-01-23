/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke;

import org.apache.wicket.protocol.http.WebApplication;

import com.locke.tricks.a.A;
import com.locke.tricks.b.B;
import com.locke.tricks.c.C;
import com.locke.tricks.e.E;
import com.locke.tricks.f.F;
import com.locke.tricks.g.G;
import com.locke.tricks.h.H;
import com.locke.tricks.i.I;
import com.locke.tricks.l.L;
import com.locke.tricks.m.M;
import com.locke.tricks.s.S;
import com.locke.tricks.t.T;
import com.locke.tricks.u.U;
import com.locke.tricks.v.V;
import com.locke.tricks.w.W;
import com.locke.tricks.x.X;
import com.locke.tricks.y.Y;

// TODO Reviewers: Scott Swank

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.locke.Start#main(String[])
 */
public class TwentySixWicketTricks extends WebApplication {

	/**
	 * Constructor
	 */
	public TwentySixWicketTricks() {
		mountBookmarkablePage("/A", A.class);
		mountBookmarkablePage("/B", B.class);
		mountBookmarkablePage("/C", C.class);
		mountBookmarkablePage("/E", E.class);
		mountBookmarkablePage("/F", F.class);
		mountBookmarkablePage("/G", G.class);
		mountBookmarkablePage("/H", H.class);
		mountBookmarkablePage("/I", I.class);
		mountBookmarkablePage("/L", L.class);
		mountBookmarkablePage("/M", M.class);
		mountBookmarkablePage("/S", S.class);
		mountBookmarkablePage("/T", T.class);
		mountBookmarkablePage("/U", U.class);
		mountBookmarkablePage("/V", V.class);
		mountBookmarkablePage("/W", W.class);
		mountBookmarkablePage("/X", X.class);
		mountBookmarkablePage("/Y", Y.class);
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<A> getHomePage() {
		return A.class;
	}
}
