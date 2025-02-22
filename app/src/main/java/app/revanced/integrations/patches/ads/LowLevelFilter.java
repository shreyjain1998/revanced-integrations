package app.revanced.integrations.patches.ads;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;


public class LowLevelFilter {

    private static final List<String> ignoredList = Arrays.asList(
            "avatar",
            "compact_channel",
            "description",
            "grid_video",
            "inline_expander",
            "metadata",
            "shorts",
            "thumbnail",
            "video_action_bar",
            "video_action_button",
            "_menu",
            "-button",
            "-count",
            "-space"
    );
    private static final List<String> browseButtonPhone = Arrays.asList(
            "channel_profile_phone.eml",
            "channel_action_buttons_phone.eml",
            "|ContainerType|button.eml|"
    );
    private static final List<String> joinButtonPhone = List.of(
            "|ContainerType|ContainerType|ContainerType|button.eml|"
    );
    private static final List<String> browseButtonTablet = Arrays.asList(
            "channel_profile_tablet.eml",
            "|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|button.eml|"
    );
    public static ByteBuffer byteBuffer;

    public static boolean filter(String path, String value) {
        if (ignoredList.stream().anyMatch(path::contains))
            return false;

        int count = 0;
        if (PatchStatus.LayoutComponent()) {
            // Browse store button needs a bit of a tricky filter
            if (SettingsEnum.HIDE_BROWSE_STORE_BUTTON.getBoolean() &&
                    ((browseButtonPhone.stream().allMatch(path::contains) &&
                            joinButtonPhone.stream().noneMatch(path::contains)) ||
                            browseButtonTablet.stream().allMatch(path::contains)))
                count++;

            // Survey banners are shown everywhere, so we handle them in low-level filters
            // e.g. Home Feed, Search Results, Related Videos, Comments, and Shorts
            if (SettingsEnum.HIDE_FEED_SURVEY.getBoolean() &&
                    value.contains("_survey")) count++;

            // Descriptions banner at the bottom of thumbnails are also shown in various places
            if (SettingsEnum.HIDE_GRAY_DESCRIPTION.getBoolean() &&
                    path.contains("endorsement_header_footer")) count++;

            // Official header of the search results can be identified through another byteBuffer
            if (SettingsEnum.HIDE_OFFICIAL_HEADER.getBoolean() &&
                    Stream.of("shelf_header")
                            .allMatch(value::contains) &&
                    Stream.of("YTSans-SemiBold", "sans-serif-medium")
                            .allMatch(new String(byteBuffer.array(), StandardCharsets.UTF_8)::contains))
                count++;
        }

        if (PatchStatus.DescriptionComponent()) {
            // As a limitation of the implementation of RVX patches, this too must be filtered in a low-level filter
            if (SettingsEnum.HIDE_CHAPTERS.getBoolean() &&
                    path.contains("macro_markers_carousel.")) count++;
        }

        if (PatchStatus.SuggestedActions() && !PlayerType.getCurrent().isNoneOrHidden()) {
            // It is a single filter to separate into independent patches
            if (SettingsEnum.HIDE_SUGGESTED_ACTION.getBoolean() &&
                    value.contains("suggested_action")) count++;
        }

        return count > 0;
    }
}
