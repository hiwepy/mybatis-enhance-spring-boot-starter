/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.mybatis.dbperms;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(MybatisDbpermsProperties.PREFIX)
@Getter
@Setter
@ToString
public class MybatisDbpermsProperties {

	public static final String PREFIX = "mybatis-dbperms";
	
	/**
	 * 缓存过期时间
	 */
	private int expire = 30;
	
	/**
	 * 缓存过期时间单位
	 */
	private TimeUnit unit = TimeUnit.MINUTES;
	
	
}
