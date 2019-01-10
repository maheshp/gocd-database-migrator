/*
 * Copyright 2020 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.dbsync.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.thoughtworks.go.dbsync.DbSync;

import static org.jooq.tools.StringUtils.isBlank;

public class Main {

    public static void main(String[] argv) {
        Args args = new Args();
        JCommander commander = JCommander.newBuilder()
                .addObject(args)
                .programName("gocd-h2-db-export")
                .build();
        try {
            commander.parse(argv);

            if (args.help) {
                printUsageAndExit(commander);
            } else {
                if (isBlank(args.outputFile) && !args.insert) {
                    commander.getConsole().println("ERROR: At least one of `--output` or `--insert` options must be specified.");
                    printUsageAndExit(commander);
                }
                new DbSync(args).export();
            }
        } catch (ParameterException e) {
            commander.getConsole().println("ERROR: " + e.getMessage());
            printUsageAndExit(commander);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsageAndExit(JCommander commander) {
        commander.usage();
        System.exit(1);
    }
}
