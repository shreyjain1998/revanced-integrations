package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class QuickActionFilter extends Filter {
    public QuickActionFilter() {
        pathFilterGroups.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_LIKE_BUTTON,
                        "|like_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_DISLIKE_BUTTON,
                        "dislike_button"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_RELATED_VIDEO,
                        "fullscreen_related_videos"
                )
        );

        protobufBufferFilterGroups.addAll(
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_LIKE_BUTTON,
                        "yt_outline_thumb_up"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_DISLIKE_BUTTON,
                        "yt_outline_thumb_down"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_COMMENT_BUTTON,
                        "yt_outline_message_bubble_right"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_LIVE_CHAT_BUTTON,
                        "yt_outline_message_bubble_overlap"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_PLAYLIST_BUTTON,
                        "yt_outline_library_add"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_SHARE_BUTTON,
                        "yt_outline_share"
                ),
                new ByteArrayAsStringFilterGroup(
                        SettingsEnum.HIDE_QUICK_ACTIONS_MORE_BUTTON,
                        "yt_outline_overflow_horizontal"
                )
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier, final String allValue, final byte[] _protobufBufferArray) {
        if (path.startsWith("quick_actions.eml|"))
            return super.isFiltered(path, identifier, allValue, _protobufBufferArray);

        return false;
    }
}
