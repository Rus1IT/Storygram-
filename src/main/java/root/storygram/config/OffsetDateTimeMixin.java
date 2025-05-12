package root.storygram.config;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
public abstract class OffsetDateTimeMixin {
}

