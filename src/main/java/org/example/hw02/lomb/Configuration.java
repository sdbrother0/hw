package org.example.hw02.lomb;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
public class Configuration {
    private String setting1;
    private String setting2;
    private boolean isFlag1;
    private boolean isFlag2;
}
